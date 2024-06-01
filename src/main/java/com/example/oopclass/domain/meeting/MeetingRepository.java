package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Optional<Meeting> findByMeetingUuidAndDeletedAtIsNull(UUID meetingUuid);

    @Query("SELECT m FROM Meeting m WHERE " +
            "(:majorUuid IS NULL OR m.major.majorUuid = :majorUuid) AND " +
            "(:subjectUuid IS NULL OR m.subject.subjectUuid = :subjectUuid) AND " +
            "(:teamType IS NULL OR m.teamType = :teamType) AND " +
            "(:desiredCount IS NULL OR m.desiredCount = :desiredCount) AND " +
            "(:searchText IS NULL OR m.subject.subjectName LIKE %:searchText% OR m.user.username LIKE %:searchText%)")
    List<Meeting> filterAndSearchMeetings(@Param("majorUuid") UUID majorUuid,
                                          @Param("subjectUuid") UUID subjectUuid,
                                          @Param("teamType") String teamType,
                                          @Param("desiredCount") Integer desiredCount,
                                          @Param("searchText") String searchText);
}

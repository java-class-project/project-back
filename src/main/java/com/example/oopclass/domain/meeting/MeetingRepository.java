package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Optional<Meeting> findByMeetingUuidAndDeletedAtIsNull(UUID meetingUuid);

    List<Meeting> findByUser_UserUuid(UUID userUuid);

    @Query("SELECT m FROM Meeting m " +
            "LEFT JOIN m.meetingInfo mi " +
            "WHERE (:majorUuid IS NULL OR m.major.majorUuid = :majorUuid) AND " +
            "(:subjectUuid IS NULL OR m.subject.subjectUuid = :subjectUuid) AND " +
            "(COALESCE(:teamTypes) IS NULL OR m.teamType IN :teamTypes) AND " +
            "(:desiredCount IS NULL OR m.desiredCount = :desiredCount) AND " +
            "(:classNum = 0 OR m.classNum = :classNum) AND " + // Add classNum filter
            "(:searchText IS NULL OR m.title LIKE %:searchText% OR m.description LIKE %:searchText%) AND " +
            "(:status IS NULL OR " +
            "(:status = 'person' AND mi.meetingRecruitmentFinished = 1) OR " +
            "(:status = 'member' AND mi.meetingRecruitmentFinished > 1))")
    List<Meeting> filterAndSearchMeetings(@Param("majorUuid") UUID majorUuid,
                                          
                                          @Param("subjectUuid") UUID subjectUuid,
                                          @Param("teamTypes") List<String> teamTypes,
                                          @Param("desiredCount") Integer desiredCount,
                                          @Param("classNum") Integer classNum, // Add classNum parameter
                                          @Param("searchText") String searchText,
                                          @Param("status") String status);
}
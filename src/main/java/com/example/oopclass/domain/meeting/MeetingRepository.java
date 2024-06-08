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
            "(COALESCE(:teamTypes, null) IS NULL OR m.teamType IN :teamTypes) AND " +
            "(:desiredCount IS NULL OR m.desiredCount = :desiredCount) AND " +
            "(:classNum = 0 OR m.classNum = :classNum) AND " +
            "(:searchText IS NULL OR m.title LIKE %:searchText% OR m.description LIKE %:searchText%) AND " +
            "(COALESCE(:status, null) IS NULL OR " +
            "(CASE WHEN 'person' IN (:status) THEN mi.meetingRecruitmentFinished = 1 ELSE TRUE END) AND " +
            "(CASE WHEN 'team' IN (:status) THEN mi.meetingRecruitmentFinished > 1 ELSE TRUE END))")
    List<Meeting> filterAndSearchMeetings(@Param("majorUuid") UUID majorUuid,
                                          @Param("subjectUuid") UUID subjectUuid,
                                          @Param("teamTypes") List<String> teamTypes,
                                          @Param("desiredCount") Integer desiredCount,
                                          @Param("classNum") Integer classNum,
                                          @Param("searchText") String searchText,
                                          @Param("status") List<String> status);

}
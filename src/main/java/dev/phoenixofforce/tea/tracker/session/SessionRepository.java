package dev.phoenixofforce.tea.tracker.session;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("Select s from Session s where :teaId is null or s.tea.id = :teaId")
    List<Session> findByTeaId(Long teaId);

    @Query("""
        Select s from Session s
        where s.id = (
            Select MAX(s.id)
            from Session s
            where s.id < :sessionId
        )
        """)
    Optional<Session> findLastSessionBeforeId(Long sessionId);

    @Query("""
            Select distinct s.brewingMethod
            from Session s
            where lower(s.brewingMethod) like lower(concat('%', :query, '%'))
            order by s.brewingMethod
        """)
    List<String> findBrewingMethods(String query, Limit of);
}

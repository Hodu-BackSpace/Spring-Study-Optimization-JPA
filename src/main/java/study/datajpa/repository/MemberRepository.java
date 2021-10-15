package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;
import study.datajpa.dto.MemberDto;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    Long countAllBy();

    List<Member> findByUsername(String username);

    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m.username from Member m where m.username in :names")
    List<String> findByNames(@Param("names") Collection<String> names);


    List<Member> findByUsernameIn(Collection<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalByUsername(String username);

    // Total Count가 필요한 경우 Page<T> 사용
    // Total Count가 필요하지 않은 경우 Slice<T> 사용

    @Query(value = "select m from Member m left join m.team t where m.age=:age",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(@Param("age") int age, Pageable pageable);


    Page<Member> findAllBy(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // Lazy Loading
    @Query("select m from Member m join fetch m.team t")
    List<Member> findLazyAll();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Member findLockByUsername(String username);
}

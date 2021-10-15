package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;


    public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
        return em.createQuery("select m from Member m where m.username=:username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public void saveAll(List<Member> members) {
        members.forEach(m -> em.persist(m));

    }

//    public List<Member> findByUsername(String username) {
//        return em.createNamedQuery("Member.findByUsername", Member.class)
//                .setParameter("username", username)
//                .getResultList();
//    }
    public List<Member> findByPage(int age,int offset,int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc ", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    // count 쿼리에는 sorting이 필요없다.
    public Long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }



}


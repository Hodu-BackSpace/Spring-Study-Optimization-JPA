package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @Autowired
    EntityManager em;


    @Test
    public void testEntity() {
        Team teamA = Team.createTeamEntity()
                .name("teamA")
                .build();

        Team teamB = Team.createTeamEntity()
                .name("teamB")
                .build();

        em.persist(teamA);
        em.persist(teamB);

        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .team(teamA)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(20)
                .team(teamA)
                .build();

        Member memberC = Member.createMemberEntity()
                .username("memberC")
                .age(30)
                .team(teamB)
                .build();

        Member memberD = Member.createMemberEntity()
                .username("memberD")
                .age(40)
                .team(teamB)
                .build();

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        // 초기화
        em.flush();
        em.clear();

        // 확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        members.forEach(member -> {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        });
    }
}
package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.dto.MemberDto;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EntityManager em;

    @Test
    public void findMemberDto() {
        Team teamA = Team.createTeamEntity()
                .name("teamA")
                .build();

        teamRepository.save(teamA);

        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .team(teamA)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberA")
                .age(30)
                .team(teamA)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        List<MemberDto> result = memberRepository.findMemberDto();

        result.forEach(r -> System.out.println("r = " + r));

    }

    @Test
    public void findReturnTypes() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(30)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        List<Member> result1 = memberRepository.findListByUsername("memberA");

        Optional<Member> result2 = memberRepository.findOptionalByUsername("memberA");

        Member result3 = memberRepository.findMemberByUsername("memberA");

        System.out.println("result1 = " + result1);
        System.out.println("result2 = " + result2);
        System.out.println("result3 = " + result3);
    }

    @Test
    public void findByUsernames() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(30)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        List<Member> result = memberRepository.findByUsernameIn(Arrays.asList("memberA", "memberB","memberC"));

        System.out.println("result = " + result);

        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void findByNames() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(30)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        List<String> result = memberRepository.findByNames(Arrays.asList("memberA", "memberB"));

        System.out.println("result = " + result);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findByUsername() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberA")
                .age(30)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        List<Member> result = memberRepository.findByUsername("memberA");
        assertThat(result.size()).isEqualTo(2);
    }
    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberA")
                .age(30)
                .build();


        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 25);

        assertThat(result.get(0)).isSameAs(memberB);
    }


    @Test
    public void testMember() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .build();

        Member savedMember = memberRepository.save(memberA);

        Member findMember = memberRepository.findById(memberA.getId()).get();

        assertThat(savedMember).isSameAs(findMember);
        assertThat(savedMember.getUsername()).isEqualTo(findMember.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB));

        // 단건 조회 검사
        Optional<Member> findMemberA = memberRepository.findById(memberA.getId());
        Optional<Member> findMemberB = memberRepository.findById(memberB.getId());

        assertThat(findMemberA.get()).isSameAs(memberA);
        assertThat(findMemberB.get()).isSameAs(memberB);

        // 리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        Long aLong = memberRepository.countAllBy();
        System.out.println("aLong = " + aLong);

    }

    @Test
    void findByPageTest() throws Exception {
        //given

        Team teamA = Team.createTeamEntity()
                .name("teamA")
                .build();

        teamRepository.save(teamA);

        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .build();
        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(10)
                .build();
        Member memberC = Member.createMemberEntity()
                .username("memberC")
                .age(10)
                .team(teamA)
                .build();

        Member memberD = Member.createMemberEntity()
                .username("memberD")
                .age(10)
                .build();
        Member memberE = Member.createMemberEntity()
                .username("memberE")
                .age(10)
                .build();


        memberRepository.saveAll(Arrays.asList(memberA, memberB, memberC, memberD, memberE));

        em.flush();
        em.clear();

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> result = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> resultMap = result.map(MemberDto::of);

        //then
        List<Member> content = result.getContent();

        Pageable pageable = result.nextPageable();

        assertThat(content.size()).isEqualTo(3);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getNumber()).isEqualTo(0); // Page 번호
        assertThat(result.getTotalPages()).isEqualTo(2); // 총 Page 번호
        assertThat(result.isFirst()).isEqualTo(true); // 첫번째 페이지 인가?
        assertThat(result.hasNext()).isEqualTo(true); // 다음 페이지가 있는가?

    }

    @Test
    void bulkUpdateTest() throws Exception {
        //given
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .build();
        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(19)
                .build();
        Member memberC = Member.createMemberEntity()
                .username("memberC")
                .age(20)
                .build();
        Member memberD = Member.createMemberEntity()
                .username("memberD")
                .age(21)
                .build();
        Member memberE = Member.createMemberEntity()
                .username("memberE")
                .age(40)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB, memberC, memberD, memberE));

        //when
        int result = memberRepository.bulkAgePlus(20);

        //then
        assertThat(result).isEqualTo(3);

    }

    @Test
    void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        // member2 -> teamB

        Team teamA = Team.createTeamEntity()
                .name("teamA")
                .build();

        Team teamB = Team.createTeamEntity()
                .name("teamB")
                .build();

        teamRepository.saveAll(Arrays.asList(teamA, teamB));

        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .team(teamA)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberB")
                .age(10)
                .team(teamB)
                .build();

        Member memberC = Member.createMemberEntity()
                .username("memberC")
                .age(10)
                .build();

        memberRepository.saveAll(Arrays.asList(memberA, memberB, memberC));

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findLazyAll();
        List<Member> result = memberRepository.findEntityGraphByUsername("memberA");
        result.forEach(r -> {
            System.out.println("r = " + r);
        });
        members.forEach(m -> {
            System.out.println("m = " + m.getTeam());
        });
        //then

    }

    @Test
    void queryHint() throws Exception {
        //given
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .build();
        memberRepository.save(memberA);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(memberA.getUsername());

        findMember.changeUsername("memberB");
        em.flush();
        //when

        //then

    }

    @Test
    void findLockByUsername() throws Exception {
        //given
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .build();
        memberRepository.save(memberA);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findLockByUsername("memberA");

        //then

    }

    @Test
    void callCustom() throws Exception {
        //given
        List<Member> result = memberRepository.findMemberCustom();

        //when
        assertThat(result.size()).isEqualTo(0);

        //then

    }

    @Test
    void jpaAuditingTest() throws Exception {
        //given
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(10)
                .build();

        memberRepository.save(memberA);

        //when
        Optional<Member> findMember = memberRepository.findById(1L);

        Member result = findMember.orElseThrow();

        Thread.sleep(1000);

        result.changeUsername("memberB");

        em.flush();
        em.clear();

        Optional<Member> findMember2 = memberRepository.findById(1L);
        Member result2 = findMember2.orElseThrow();
        //then
        System.out.println("result.getCreatedDate() = " + result2.getCreatedDate());
        System.out.println("result.getLastModifiedDate() = " + result2.getLastModifiedDate());
        System.out.println("result2.getCreatedBy() = " + result2.getCreatedBy());
        System.out.println("result2.getLastModifiedBy() = " + result2.getLastModifiedBy());

    }

}
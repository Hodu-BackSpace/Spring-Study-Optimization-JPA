package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

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


        memberJpaRepository.saveAll(Arrays.asList(memberA, memberB));

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("memberA", 25);

        assertThat(result.get(0)).isSameAs(memberB);
    }

    @Test
    public void namedQueryTest() {
        Member memberA = Member.createMemberEntity()
                .username("memberA")
                .age(20)
                .build();

        Member memberB = Member.createMemberEntity()
                .username("memberA")
                .age(30)
                .build();


        memberJpaRepository.saveAll(Arrays.asList(memberA, memberB));

//        List<Member> result = memberJpaRepository.findByUsername("memberA");

//        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    void findByPageTest() throws Exception {
        //given
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
                .build();
        Member memberD = Member.createMemberEntity()
                .username("memberD")
                .age(10)
                .build();

        memberJpaRepository.saveAll(Arrays.asList(memberA,memberB,memberC,memberD));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> result = memberJpaRepository.findByPage(age, offset, limit);
        Long totalCount = memberJpaRepository.totalCount(age);

        //then
        assertThat(result.size()).isEqualTo(limit);
        assertThat(totalCount).isEqualTo(4);
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

        memberJpaRepository.saveAll(Arrays.asList(memberA, memberB, memberC, memberD, memberE));

        //when
        int result = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(result).isEqualTo(3);


    }

}
package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow();

        memberRepository.findOptionalByUsername("memberA");

        return findMember.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> memberList(@RequestParam(value = "page", required = false, defaultValue = "0") int num) {
        PageRequest page = PageRequest.of(num, 5, Sort.by(Sort.Direction.ASC, "username"));
        return memberRepository.findAllBy(page)
                .map(MemberDto::of);
    }

    @PostConstruct
    public void init() {

        for (int i = 0; i < 20; i++) {
            memberRepository.save(Member.createMemberEntity()
                    .username("member" + i)
                    .age(i*3)
                    .build());
        }
    }
}

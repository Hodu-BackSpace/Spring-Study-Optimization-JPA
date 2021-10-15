package study.datajpa.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.datajpa.entity.Member;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public static MemberDto of(Member member) {
        if (member.getTeam() != null) {
            return new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName());
        }
        return new MemberDto(member.getId(), member.getUsername(), null);
    }

}

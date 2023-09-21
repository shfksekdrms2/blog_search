package domain.solution.core.model.controller;

import domain.solution.core.model.page.PageableRs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class BlogSearchRs extends PageableRs {

    @Schema(description = "내용")
    List<BlogSearchDto> documents = new ArrayList<>();

    public static BlogSearchRs of(PageImpl<BlogSearchDto> blogSearchList) {
        BlogSearchRs rs = new BlogSearchRs();
        rs.setDocuments(blogSearchList.getContent());
        rs.setPageInfo(blogSearchList);
        return rs;
    }
}

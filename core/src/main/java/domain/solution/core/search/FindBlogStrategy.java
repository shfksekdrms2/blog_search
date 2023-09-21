package domain.solution.core.search;

import domain.solution.core.model.controller.BlogSearchRs;
import domain.solution.core.model.controller.SortType;
import org.springframework.data.domain.PageRequest;

public interface FindBlogStrategy {
    BlogSearchRs findBlog(String keyword,
                          SortType sortType,
                          PageRequest pageRequest);
}

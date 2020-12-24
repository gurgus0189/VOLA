package com.vitcon.service.tui.grid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"page",
"totalCount"
})
public class Pagination {

@JsonProperty("page")
private Integer page;
@JsonProperty("totalCount")
private Integer totalCount;

@JsonProperty("page")
public Integer getPage() {
return page;
}

@JsonProperty("page")
public void setPage(Integer page) {
this.page = page;
}

@JsonProperty("totalCount")
public Integer getTotalCount() {
return totalCount;
}

@JsonProperty("totalCount")
public void setTotalCount(Integer totalCount) {
this.totalCount = totalCount;
}

}
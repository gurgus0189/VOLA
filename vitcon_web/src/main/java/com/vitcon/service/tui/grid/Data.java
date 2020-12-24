package com.vitcon.service.tui.grid;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"contents",
"pagination"
})
public class Data<T> {

@JsonProperty("contents")
private List<T> contents = null;
@JsonProperty("pagination")
private Pagination pagination;

@JsonProperty("contents")
public List<T> getContents() {
return contents;
}

@JsonProperty("contents")
public void setContents(List<T> contents) {
this.contents = contents;
}

@JsonProperty("pagination")
public Pagination getPagination() {
return pagination;
}

@JsonProperty("pagination")
public void setPagination(Pagination pagination) {
this.pagination = pagination;
}

}
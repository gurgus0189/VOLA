package com.vitcon.service.tui.grid;

import org.apache.ibatis.annotations.Select;

public interface CommonMapper {

	@Select("SELECT FOUND_ROWS()")
	public int pagingTotal();
}

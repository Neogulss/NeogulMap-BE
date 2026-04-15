package com.neogulss.neogulmap.report.mapper;

import com.neogulss.neogulmap.report.dto.ReportDTO;
import com.neogulss.neogulmap.report.dto.SalesPredDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalesPredMapper {

    SalesPredDTO.DbResult selectSalesPredFeatures(ReportDTO.Request request);

}

package com.project.manager.dto;

import java.util.List;

public record PageRecordDto(int actualPage,
                            long totalRecords,
                            int totalPages,
                            List<?> itemList) {
}

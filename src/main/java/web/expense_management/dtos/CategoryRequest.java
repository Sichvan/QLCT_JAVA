package web.expense_management.dtos;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String type;
    private String icon;
}
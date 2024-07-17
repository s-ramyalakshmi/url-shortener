package com.urlshortener.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UrlMapping {
    private Long id;
    private String longURL;
    private String shortURL;
    private LocalDateTime createdAt;
    private LocalDateTime expiryDate;
}

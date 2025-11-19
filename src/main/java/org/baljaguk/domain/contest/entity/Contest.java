package org.baljaguk.domain.contest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.baljaguk.domain.category.entity.ContestCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name = "contests")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "site_url", nullable = false)
    private String siteUrl;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "categories", nullable = false)
    private String categories; // 공모분야

    @Column(name = "benefits", nullable = true)
    private String benefits; // 활동 혜택

    @Column(name = "award_scale", nullable = true)
    private String awardScale; // 시상규모

    @Column(name = "company_type", nullable = true)
    private String companyType; // 기업형태

    @Column(name = "target_participants", nullable = true)
    private String targetParticipants; // 참여대상

    @Column(name = "additional_benefits", nullable = true)
    private String additionalBenefits; // 추가혜택
}

package dev.phoenixofforce.tea.tracker.web_extraction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "extraction_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "profile_id", nullable = false)
    private List<RawFieldSetting> settings;

    @Convert(converter = StringSplitConverter.class)
    private List<String> validUrls;

    // Todo: Source url for auto update

}

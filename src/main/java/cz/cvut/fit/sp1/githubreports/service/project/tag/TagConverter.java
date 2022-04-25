package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.api.dto.project.TagDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitService;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TagConverter {

    private final CommitService commitService;
    private final ProjectService projectService;

    public Tag toModel(TagDTO tagDTO) {
        return new Tag(
                tagDTO.getTagID(),
                tagDTO.getTagName(),
                projectService.readById(tagDTO.getProjectID()).orElseThrow(RuntimeException::new),
                tagDTO.getCommitsIDs().stream().map(tagID -> commitService.readById(tagID).orElseThrow(RuntimeException::new)).collect(Collectors.toList())
        );
    }

    public TagDTO fromModel(Tag tag) {
        return new TagDTO(
                tag.getTagId(),
                tag.getTagName(),
                tag.getProject().getProjectId(),
                tag.getCommits().stream().map(Commit::getCommitId).collect(Collectors.toList())
        );
    }

    public List<Tag> toModelsMany(List<TagDTO> tagDTOs) {
        return tagDTOs.stream().map(this::toModel).collect(Collectors.toList());
    }

    public List<TagDTO> fromModelsMany(List<Tag> tags) {
        return tags.stream().map(this::fromModel).collect(Collectors.toList());
    }
}

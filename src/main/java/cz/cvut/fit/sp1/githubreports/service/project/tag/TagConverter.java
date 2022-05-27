package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.api.dto.project.TagDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitSPI;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TagConverter {

    private final CommitSPI commitSPI;
    private final ProjectSPI projectSPI;

    public Tag toModel(TagDTO tagDTO) {
        return new Tag(
                tagDTO.getTagID(),
                tagDTO.getTagName(),
                projectSPI.readById(tagDTO.getProjectID()).orElseThrow(IncorrectRequestException::new),
                tagDTO.getCommitsIDs().stream().map(tagID -> commitSPI.readById(tagID).orElseThrow(IncorrectRequestException::new)).collect(Collectors.toList())
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

    public Collection<Tag> toModelsMany(Collection<TagDTO> tagDTOs) {
        return tagDTOs.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<TagDTO> fromModelsMany(Collection<Tag> tags) {
        return tags.stream().map(this::fromModel).collect(Collectors.toList());
    }
}

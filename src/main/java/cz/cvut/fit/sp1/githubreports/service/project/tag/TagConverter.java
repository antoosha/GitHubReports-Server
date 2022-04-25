package cz.cvut.fit.sp1.githubreports.service.project.tag;

import cz.cvut.fit.sp1.githubreports.api.dto.project.TagDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.service.project.commit.CommitService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagConverter {

    private final CommitService commitService;

    public TagConverter(CommitService commitService){
        this.commitService = commitService;
    }

    public Tag toModel(TagDTO tagDTO){
        return new Tag(
            tagDTO.getTagID(),
            tagDTO.getTagName(),
            tagDTO.getProjectID(),
            tagDTO.getCommitsIDs().stream().map(x -> commitService.getById(x)).collect(Collectors.toList())
        );
    }

    public TagDTO fromModel(Tag tag){
        return new TagDTO(
            tag.getTagId(),
            tag.getTagName(),
            tag.getProject(),
            tag.getCommits().stream().map(x -> getCommitId(x)).collect(Collectors.toList())
        );
    }

    public List<Tag> toModelMany(List<TagDTO> tags){
        return tags.stream().map(this::toModel).collect(Collectors.toList());
    }

    public List<TagDTO> fromModelMany(List<Tag> tags){
        return tags.stream().map(this::fromModel).collect(Collectors.toList());
    }
}

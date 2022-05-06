package cz.cvut.fit.sp1.githubreports.api.controller.project;


import cz.cvut.fit.sp1.githubreports.api.dto.project.CommentDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.project.Comment;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentConverter;
import cz.cvut.fit.sp1.githubreports.service.project.comment.CommentSPI;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentSPI commentSPI;
    private final CommentConverter commentConverter;

    @GetMapping()
    public Collection<CommentDTO> getAll() {
        return commentConverter.fromModelsMany((List<Comment>) commentSPI.readAll());
    }

    @GetMapping("/{id}")
    public CommentDTO getOne(@PathVariable Long id) {
        return commentConverter.fromModel(commentSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public CommentDTO create(@RequestBody CommentDTO commentDTO) throws EntityStateException {
        return commentConverter.fromModel(commentSPI.create(commentConverter.toModel(commentDTO)));
    }

    @PutMapping("/{id}")
    public CommentDTO update(@PathVariable Long id, @RequestBody CommentDTO commentDTO) throws IncorrectRequestException, EntityStateException {
        if (!commentDTO.getCommitID().equals(id))
            throw new IncorrectRequestException();
        return commentConverter.fromModel(commentSPI.update(commentDTO.getCommitID(), commentConverter.toModel(commentDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (commentSPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        commentSPI.delete(id);
    }
}


package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagMapper;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagSPI;
import lombok.RequiredArgsConstructor;
import org.openapi.api.TagsApi;
import org.openapi.model.TagDTO;
import org.openapi.model.TagSlimDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TagsApiDelegateImpl implements TagsApi {

    private final TagSPI tagSPI;

    private final TagMapper tagMapper;

    @Override
    public ResponseEntity<Void> deleteTag(Long id) {
        tagSPI.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TagDTO> getTag(Long id) {
        return ResponseEntity.ok(
            tagMapper.toDTO(tagSPI.readById(id)));
    }

    @Override
    public ResponseEntity<TagDTO> updateTag(Long id, TagSlimDTO tagSlimDTO) {
        return ResponseEntity.ok(
            tagMapper.toDTO(tagSPI.update(id, tagMapper.fromSlimDTO(tagSlimDTO))));
    }
}

package server.api;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private TagService tagService;

    /**
     * Constructor for tag controller
     * @param tagService - the tag service for the tag repository
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
        tagService.add(new Tag("food", "#93C47D"));
        tagService.add(new Tag("entrance fees", "#4A86E8"));
        tagService.add(new Tag("travel", "#E06666"));
    }

    /**
     * Get all tags
     * @return all tags
     */
    @GetMapping(path = {"", "/"})
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    /**
     * Save a new tag if it is valid and returns a response
     * @param tag - the tag to be saved
     * @return response entity with "ok" status message if it is successful,
     * "bad request" message otherwise
     */
    @PostMapping("/add")
    public ResponseEntity<Tag> add(@RequestBody Tag tag){
        Tag tagSaved = tagService.add(tag);
        if(tagSaved == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(tagSaved);
        }
    }

    /**
     * Update a tag if it is valid
     * @param tag - the new tag
     * @return response entity with "ok" status message if it is successful,
     * "bad request" message otherwise
     */
    @PostMapping("/update")
    public ResponseEntity<Tag> update(@RequestBody Tag tag) {
        Tag updatedTag = tagService.update(tag);
        if(updatedTag == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok(updatedTag);
        }
    }

    /**
     * Deletes a tag by id if it exists
     * @param id - the id of the tag we search for
     * @return "Successful delete" response
     * if delete was successful, "Bad request" otherwise
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        Tag deletedTag = tagService.delete(id);
        if (deletedTag == null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.ok().body("Successful delete");
        }
    }
}

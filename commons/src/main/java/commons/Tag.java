package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String type;
    private String color;

    /**
     * Constructor for Tag
     * @param type - the type of the tag
     * @param color - the color of the tag
     */
    public Tag(String type, String color) {
        this.type = type;
        this.color = color;
    }

    /**
     *  Empty constructor
     */
    public Tag() {
        // empty
    }

    /**
     * Getter for id
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the type of tag
     * @return the type of the tag
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for the color of the tag
     * @return the color of the tag
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for the type of the tag
     * @param type - the type to be set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Setter for the color of the tag
     * @param color - the color to be set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Equals method for tag
     * @param o - the object we compare with
     * @return true if the objects are equal,
     * false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(type, tag.type) &&
            Objects.equals(color, tag.color);
    }

    /**
     * Hash methode for tag
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, type, color);
    }
}

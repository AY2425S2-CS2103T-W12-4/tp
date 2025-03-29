package tassist.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import tassist.address.commons.core.index.Index;
import tassist.address.commons.util.StringUtil;
import tassist.address.logic.parser.exceptions.ParseException;
import tassist.address.model.person.ClassNumber;
import tassist.address.model.person.Email;
import tassist.address.model.person.Github;
import tassist.address.model.person.Name;
import tassist.address.model.person.Phone;
import tassist.address.model.person.Progress;
import tassist.address.model.person.ProjectTeam;
import tassist.address.model.person.StudentId;
import tassist.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }


    /**
     * Parses a {@code String github link} into a {@code Github}.
     * Leading and trailing white spaces will be trimmed.
     *
     * @throws ParseException if the given {@code github} is invalid.
     */
    public static Github parseGithub(String github) throws ParseException {
        requireNonNull(github);
        String trimmedGithub = github.trim();
        if (!Github.isValidGithub(trimmedGithub)) {
            throw new ParseException(Github.MESSAGE_CONSTRAINTS);
        }
        return new Github(trimmedGithub);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String class number} into a {@code ClassNumber}.
     * Leading and trailing white spaces will be trimmed.
     *
     * @throws ParseException if the given {@code class number} is invalid.
     */
    public static ClassNumber parseClassNumber(String classNumber) throws ParseException {
        requireNonNull(classNumber);
        String trimmedClassNumber = classNumber.trim();
        if (!ClassNumber.isValidClassNumber(trimmedClassNumber)) {
            throw new ParseException(ClassNumber.MESSAGE_CONSTRAINTS);
        }
        return new ClassNumber(trimmedClassNumber);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static StudentId parseStudentId(String studentId) throws ParseException {
        requireNonNull(studentId);
        String trimmedStudentId = studentId.trim();
        if (!StudentId.isValidStudentId(trimmedStudentId)) {
            throw new ParseException(StudentId.MESSAGE_CONSTRAINTS);
        }
        return new StudentId(trimmedStudentId);
    }

    /**
     * Parses a {@code String projectTeam} into an {@code ProjectTeam}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code projectTeam} is invalid.
     */
    public static ProjectTeam parseProjectTeam(String projectTeam) throws ParseException {
        requireNonNull(projectTeam);
        String trimmedProjectTeam = projectTeam.trim();
        if (!ProjectTeam.isValidProjectTeam(trimmedProjectTeam)) {
            throw new ParseException(ProjectTeam.MESSAGE_CONSTRAINTS);
        }
        return new ProjectTeam(trimmedProjectTeam);
    }

    /**
     * Parses a {@code String progress} into a {@code Progress}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code progress} is invalid.
     */
    public static Progress parseProgress(String progress) throws ParseException {
        requireNonNull(progress);
        String trimmedProgress = progress.trim();

        if (!Progress.isValidProgress(trimmedProgress)) {
            throw new ParseException(Progress.MESSAGE_CONSTRAINTS);
        }
        return new Progress(trimmedProgress);
    }

    /**
     * Parses a {@code String filePath} into a {@code Path}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Path parseFilePath(String filePath) {
        requireNonNull(filePath);
        String trimmedFilePath = filePath.trim();

        Path path = Paths.get(trimmedFilePath);
        if (path.isAbsolute()) {
            return path;
        } else {
            return path.toAbsolutePath();
        }
    }
}

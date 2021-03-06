package seedu.inventory.logic.parser;

import static java.util.Objects.requireNonNull;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.inventory.commons.core.index.Index;
import seedu.inventory.commons.util.StringUtil;
import seedu.inventory.logic.parser.exceptions.ParseException;
import seedu.inventory.model.item.FilterPrice;
import seedu.inventory.model.item.FilterQuantity;
import seedu.inventory.model.item.Image;
import seedu.inventory.model.item.Name;
import seedu.inventory.model.item.Price;
import seedu.inventory.model.item.Quantity;
import seedu.inventory.model.item.Sku;
import seedu.inventory.model.purchaseorder.RequiredDate;
import seedu.inventory.model.purchaseorder.Supplier;
import seedu.inventory.model.staff.Password;
import seedu.inventory.model.staff.Staff;
import seedu.inventory.model.staff.StaffName;
import seedu.inventory.model.staff.Username;
import seedu.inventory.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_FILEPATH_CONSTRAINTS = "File path should be a valid path.";

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
     * Parses {@code string} into a {@code String} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     */
    public static String parseString(String string) throws ParseException {
        String trimmedString = string.trim();

        return trimmedString;
    }

    //=================== Item variable parser =========================================================

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
            throw new ParseException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String price} into a {@code Price}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code price} is invalid.
     */
    public static Price parsePrice(String price) throws ParseException {
        requireNonNull(price);
        String trimmedPrice = price.trim();
        if (!Price.isValidPrice(trimmedPrice)) {
            throw new ParseException(Price.MESSAGE_PRICE_CONSTRAINTS);
        }
        return new Price(trimmedPrice);
    }

    /**
     * Parses a {@code String quantity} into a {@code Quantity}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code quantity} is invalid.
     */
    public static Quantity parseQuantity(String quantity) throws ParseException {
        requireNonNull(quantity);
        String trimmedQuantity = quantity.trim();

        if (!Quantity.isValidQuantity(trimmedQuantity)) {
            throw new ParseException(Quantity.MESSAGE_QUANTITY_CONSTRAINTS);
        }

        if (!Quantity.isNotOverflowInteger(trimmedQuantity)) {
            throw new ParseException(Quantity.MESSAGE_QUANTITY_CONSTRAINTS);
        }

        return new Quantity(trimmedQuantity);
    }

    /**
     * Parses a {@code String image} into an {@code Image}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code image} is invalid.
     */
    public static Image parseImage(String image) throws ParseException {
        requireNonNull(image);
        String trimmedImage = image.trim();
        if (!Image.isValidImage(trimmedImage)) {
            throw new ParseException(Image.MESSAGE_IMAGE_CONSTRAINTS);
        }
        return new Image(trimmedImage);
    }

    /**
     * Parses a {@code String sku} into an {@code Sku}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code sku} is invalid.
     */
    public static Sku parseSku(String sku) throws ParseException {
        requireNonNull(sku);
        String trimmedSku = sku.trim();
        if (!Sku.isValidSku(trimmedSku)) {
            throw new ParseException(Sku.MESSAGE_SKU_CONSTRAINTS);
        }
        return new Sku(trimmedSku);
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
            throw new ParseException(Tag.MESSAGE_TAG_CONSTRAINTS);
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

    //=================== Filter Item variable parser ===========================================================

    /**
     * Parses a {@code String filterPrice} into a {@code FilterPrice}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static FilterPrice parseFilterPrice(String filterPrice) throws ParseException {
        requireNonNull(filterPrice);
        String trimmedPrice = filterPrice.trim();
        if (!FilterPrice.isValidFilterPrice(trimmedPrice)) {
            throw new ParseException(FilterPrice.MESSAGE_PRICE_CONSTRAINTS);
        }
        return new FilterPrice(trimmedPrice);
    }

    /**
     * Parses a {@code String filterQuantity} into a {@code FilterQuantity}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static FilterQuantity parseFilterQuantity(String filterQuantity) throws ParseException {
        requireNonNull(filterQuantity);
        String trimmedQuantity = filterQuantity.trim();
        if (!FilterQuantity.isValidFilterQuantity(trimmedQuantity)) {
            throw new ParseException(FilterQuantity.MESSAGE_QUANTITY_CONSTRAINTS);
        }
        return new FilterQuantity(trimmedQuantity);
    }

    //=================== Purchase order variable parser =========================================================

    /**
     * Parses a {@code String reqDate} into a {@code RequiredDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code reqDate} is invalid.
     */
    public static RequiredDate parseReqDate(String reqDate) throws ParseException {
        requireNonNull(reqDate);
        String trimmedDate = reqDate.trim();
        if (!RequiredDate.isValidDatePending(trimmedDate)) {
            throw new ParseException(RequiredDate.MESSAGE_DATE_CONSTRAINTS);
        }
        return new RequiredDate(trimmedDate);
    }

    /**
     * Parses a {@code String supplier} into a {@code Supplier}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code supplier} is invalid.
     */
    public static Supplier parseSupplier(String supplier) throws ParseException {
        requireNonNull(supplier);
        String trimmedSupplier = supplier.trim();
        if (!Supplier.isValidSupplier(trimmedSupplier)) {
            throw new ParseException(Supplier.MESSAGE_SUPPLIER_CONSTRAINTS);
        }
        return new Supplier(trimmedSupplier);
    }

    //=================== Staff variable parser =========================================

    /**
     * Parses a {@code String username} into an {@code Username}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code username} is invalid.
     */
    public static Username parseUsername(String username) throws ParseException {
        requireNonNull(username);
        String trimmedUsername = username.trim();
        if (!Username.isValidUsername(trimmedUsername)) {
            throw new ParseException(Username.MESSAGE_USERNAME_CONSTRAINTS);
        }
        return new Username(trimmedUsername);
    }

    /**
     * Parses a {@code String password} into an {@code Password}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code password} is invalid.
     */
    public static Password parsePassword(String password) throws ParseException {
        requireNonNull(password);
        String trimmedPassword = password.trim();
        if (!Password.isValidPassword(trimmedPassword)) {
            throw new ParseException(Password.MESSAGE_PASSWORD_CONSTRAINTS);
        }
        String hashedPassword = Password.hash(trimmedPassword);
        return new Password(hashedPassword);
    }

    /**
     * Parses a {@code String name} into an {@code StaffName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static StaffName parseStaffName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!StaffName.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new StaffName(trimmedName);
    }

    /**
     * Parses a {@code String role} into an {@code Role}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Staff.Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (role.isEmpty()) {
            throw new ParseException(Staff.Role.MESSAGE_ROLE_CONSTRAINTS);
        }
        if (trimmedRole.equals("admin")) {
            return Staff.Role.admin;
        } else if (trimmedRole.equals("manager")) {
            return Staff.Role.manager;
        } else if (trimmedRole.equals("user")) {
            return Staff.Role.user;
        } else {
            throw new ParseException(Staff.Role.MESSAGE_ROLE_CONSTRAINTS);
        }
    }

    //=================== Reporting variable parser ====================================================
    /**
     * Parses a {@code String path} into an {@code Path}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Path parsePath(String path) throws ParseException {
        requireNonNull(path);
        String trimmedPath = path.trim();
        try {
            return Paths.get(trimmedPath);
        } catch (InvalidPathException e) {
            throw new ParseException(MESSAGE_FILEPATH_CONSTRAINTS);
        }
    }
}

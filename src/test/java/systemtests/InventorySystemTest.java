package systemtests;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.inventory.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.inventory.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.inventory.ui.testutil.GuiTestAssert.assertListMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.ItemListPanelHandle;
import guitests.guihandles.ItemTableViewHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import seedu.inventory.TestApp;
import seedu.inventory.commons.core.EventsCenter;
import seedu.inventory.commons.core.index.Index;
import seedu.inventory.logic.commands.ClearCommand;
import seedu.inventory.logic.commands.SelectCommand;
import seedu.inventory.logic.commands.item.FindItemCommand;
import seedu.inventory.logic.commands.item.ListItemCommand;
import seedu.inventory.model.Inventory;
import seedu.inventory.model.Model;
import seedu.inventory.testutil.TypicalItems;
import seedu.inventory.ui.CommandBox;

/**
 * A system test class for Inventory, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class InventorySystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        waitUntilBrowserLoaded(getBrowserPanel());
        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected Inventory getInitialData() {
        return TypicalItems.getTypicalInventoryWithStaff();
    }

    /**
     * Returns the directory of the data file.
     */
    protected Path getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public ItemListPanelHandle getItemListPanel() {
        return mainWindowHandle.getItemListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public BrowserPanelHandle getBrowserPanel() {
        return mainWindowHandle.getBrowserPanel();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ItemTableViewHandle getItemTableView() {
        return mainWindowHandle.getItemTableView();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

        waitUntilBrowserLoaded(getBrowserPanel());
    }

    /**
     * Displays all items in the inventory.
     */
    protected void showAllItems() {
        executeCommand(ListItemCommand.COMMAND_WORD);
        assertEquals(getModel().getInventory().getItemList().size(), getModel().getFilteredItemList().size());
    }

    /**
     * Displays all items with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showItemsWithName(String keyword) {
        executeCommand(FindItemCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredItemList().size() < getModel().getInventory().getItemList().size());
    }

    /**
     * Selects the item at {@code index} of the displayed list.
     */
    protected void selectItem(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getItemListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all items in the inventory.
     */
    protected void deleteAllItems() {
        executeCommand(ClearCommand.COMMAND_WORD);
        assertEquals(0, getModel().getInventory().getItemList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the storage contains the same item objects as {@code expectedModel}
     * and the item list panel displays the items in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new Inventory(expectedModel.getInventory()), testApp.readStorageInventory());
        assertListMatching(getItemListPanel(), expectedModel.getFilteredItemList());
    }

    /**
     * Calls {@code BrowserPanelHandle}, {@code ItemListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        getBrowserPanel().rememberUrl();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getItemListPanel().rememberSelectedItemCard();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url remains displaying the details
     * of the previously selected item.
     * @see BrowserPanelHandle#isUrlChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getBrowserPanel().isUrlChanged());
        assertFalse(getItemListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's image is changed to display the details of the item in the item list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see ItemListPanelHandle#isSelectedItemCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        getItemListPanel().navigateToCard(getItemListPanel().getSelectedCardIndex());
        String selectedCardImage = getItemListPanel().getHandleToSelectedCard().getImage();
        String imagePath = "file:" + selectedCardImage;
        assertEquals(imagePath, getBrowserPanel().getLoadedUrl());

        assertEquals(expectedSelectedCardIndex.getZeroBased(), getItemListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's image and the selected card in the item list panel remain unchanged.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see ItemListPanelHandle#isSelectedItemCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getBrowserPanel().isUrlChanged());
        assertFalse(getItemListPanel().isSelectedItemCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus(boolean checkSyncStatus) {
        if (checkSyncStatus) {
            assertStatusBarUnchangedExceptSyncStatus();
        } else {
            StatusBarFooterHandle handle = getStatusBarFooter();
            assertFalse(handle.isSaveLocationChanged());
        }

    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        assertListMatching(getItemListPanel(), getModel().getFilteredItemList());
        //assertEquals(MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE), getBrowserPanel().getLoadedUrl());
        assertEquals(Paths.get(".").resolve(testApp.getStorageSaveLocation()).toString(),
                getStatusBarFooter().getSaveLocation());
        assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }

    /**
     * Asserts that the item table view is showing the correct model.
     */
    protected void assertShowItemTableView(Model expectedModel) {
        ItemTableViewHandle handle = getItemTableView();
        assertEquals(handle.getItemList(), expectedModel.getFilteredItemList());
    }
}

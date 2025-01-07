import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.marc.rollerhockeystats.models.Match
import com.marc.rollerhockeystats.viewmodel.MatchesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@ExperimentalCoroutinesApi
class MatchesViewModelTest {

    private lateinit var viewModel: MatchesViewModel
    private val database: FirebaseDatabase = mock(FirebaseDatabase::class.java)
    private val matchesReference: DatabaseReference = mock(DatabaseReference::class.java)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        `when`(database.getReference("matches")).thenReturn(matchesReference)
        viewModel = MatchesViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}
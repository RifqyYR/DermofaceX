package com.makassar.dermofacex

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import com.makassar.dermofacex.data.Resource
import com.makassar.dermofacex.data.response.ClassificationProbabilityResponse
import com.makassar.dermofacex.ui.fragments.CameraFragment
import com.makassar.dermofacex.ui.viewModel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File

class CameraFragmentTest {

    private lateinit var fragment: CameraFragment
    private lateinit var mockContext: Context
    private lateinit var mockContentResolver: ContentResolver
    private lateinit var mockViewModel: MainViewModel
    private lateinit var mockResources: Resources
    private lateinit var probabilityFlow: MutableStateFlow<Resource<out ClassificationProbabilityResponse>>

    @Before
    fun setUp() {
        fragment = spyk(CameraFragment())
        mockContext = mockk()
        mockResources = mockk()
        mockContentResolver = mockk()
        every { fragment.requireContext() } returns mockContext
        every { mockContext.contentResolver } returns mockContentResolver
        every { mockContext.resources } returns mockResources
        every { mockResources.getString(R.string.app_name) } returns "RAtwo SkinDeep"
        mockViewModel = mockk(relaxed = true)
        probabilityFlow =
            MutableStateFlow(Resource.Empty() as Resource<out ClassificationProbabilityResponse>)
        every { mockViewModel.probability } returns probabilityFlow
    }

    @Test
    fun `allPermissionsGranted returns true when all permissions are granted`() {
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                any(),
                Manifest.permission.CAMERA
            )
        } returns PackageManager.PERMISSION_GRANTED

        assertTrue(fragment.allPermissionsGranted())
    }

    @Test
    fun `allPermissionsGranted returns false when permissions are not granted`() {
        mockkStatic(ContextCompat::class)
        every {
            ContextCompat.checkSelfPermission(
                any(),
                Manifest.permission.CAMERA
            )
        } returns PackageManager.PERMISSION_DENIED

        assertFalse(fragment.allPermissionsGranted())
    }

    @Test
    fun `getOutputDirectory returns correct directory`() {
        val expectedFile = File("mock_path/RAtwo SkinDeep")
        every { mockContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES) } returns File("mock_path")
        every { mockContext.filesDir } returns File("mock_files_path")

        val outputDir = fragment.getOutputDirectory()
        assertEquals(expectedFile.path, outputDir.path)
    }

    @Test
    fun `readFileFromUri returns byte array when successful`() {
        val uri = mockk<Uri>()
        val expectedBytes = "test data".toByteArray()

        every { mockContentResolver.openInputStream(uri) } returns ByteArrayInputStream(
            expectedBytes
        )

        val result = fragment.readFileFromUri(uri)
        assertArrayEquals(expectedBytes, result)
    }
}
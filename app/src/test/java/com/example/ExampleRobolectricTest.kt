package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AfricanLanguage
import com.example.data.model.ProficiencyLevel
import com.example.data.repository.BaobabContentRepository
import com.example.data.repository.LinguisticProfileRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Baobabble", appName)
  }

  @Test
  fun `verify all seven African languages exist`() {
    val languages = AfricanLanguage.values()
    assertEquals(7, languages.size)
    assertTrue(languages.any { it.id == "yoruba" })
    assertTrue(languages.any { it.id == "swahili" })
    assertTrue(languages.any { it.id == "igbo" })
    assertTrue(languages.any { it.id == "hausa" })
    assertTrue(languages.any { it.id == "twi" })
    assertTrue(languages.any { it.id == "amharic" })
    assertTrue(languages.any { it.id == "zulu" })
  }

  @Test
  fun `verify Yoruba linguistic profile accuracy`() {
    val yorubaProfile = LinguisticProfileRepository.getProfile(AfricanLanguage.YORUBA)
    assertNotNull(yorubaProfile)
    // Verify 3 tones: High (Mí), Mid (Re), Low (Dò)
    assertEquals(3, yorubaProfile.toneSystem.size)
    assertTrue(yorubaProfile.toneSystem.any { it.solfaNote == "Mí" })
    assertTrue(yorubaProfile.toneSystem.any { it.solfaNote == "Re" })
    assertTrue(yorubaProfile.toneSystem.any { it.solfaNote == "Dò" })

    // Verify special letters Gb, P, Ẹ, Ọ, Ṣ
    assertTrue(yorubaProfile.alphabet.any { it.letter.startsWith("Gb") })
    assertTrue(yorubaProfile.alphabet.any { it.letter.startsWith("P") })
    assertTrue(yorubaProfile.alphabet.any { it.letter.startsWith("Ẹ") })
    assertTrue(yorubaProfile.alphabet.any { it.letter.startsWith("Ọ") })
    assertTrue(yorubaProfile.alphabet.any { it.letter.startsWith("Ṣ") })

    // Verify minimal tone set (ọkọ, ọkọ̀, ọkọ́)
    val okoSet = yorubaProfile.minimalToneSets.firstOrNull { it.baseLetters.contains("k") }
    assertNotNull(okoSet)
    assertTrue(okoSet!!.variations.any { it.word == "Ọkọ" && it.meaning == "Husband" })
    assertTrue(okoSet.variations.any { it.word == "Ọkọ̀" && it.meaning.contains("Vehicle") })
    assertTrue(okoSet.variations.any { it.word == "Ọkọ́" && it.meaning.contains("Hoe") })
  }

  @Test
  fun `verify curriculum loading for Yoruba A1 through C2`() {
    val yorubaA1 = BaobabContentRepository.getUnitsForLanguageAndLevel(AfricanLanguage.YORUBA, ProficiencyLevel.A1)
    assertTrue(yorubaA1.isNotEmpty())

    val yorubaA2 = BaobabContentRepository.getUnitsForLanguageAndLevel(AfricanLanguage.YORUBA, ProficiencyLevel.A2)
    assertTrue(yorubaA2.isNotEmpty())

    val yorubaB1 = BaobabContentRepository.getUnitsForLanguageAndLevel(AfricanLanguage.YORUBA, ProficiencyLevel.B1)
    assertTrue(yorubaB1.isNotEmpty())

    val yorubaC1 = BaobabContentRepository.getUnitsForLanguageAndLevel(AfricanLanguage.YORUBA, ProficiencyLevel.C1)
    assertTrue(yorubaC1.isNotEmpty())

    val yorubaC2 = BaobabContentRepository.getUnitsForLanguageAndLevel(AfricanLanguage.YORUBA, ProficiencyLevel.C2)
    assertTrue(yorubaC2.isNotEmpty())
  }
}

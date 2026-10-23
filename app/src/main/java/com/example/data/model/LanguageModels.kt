package com.example.data.model

enum class AfricanLanguage(
    val id: String,
    val displayName: String,
    val nativeName: String,
    val greeting: String,
    val regions: String,
    val flagEmoji: String,
    val description: String,
    val toneOrScriptNote: String,
    val sampleProverb: String,
    val proverbTranslation: String
) {
    YORUBA(
        id = "yoruba",
        displayName = "Yoruba",
        nativeName = "Èdè Yorùbá",
        greeting = "Ẹ n lẹ o!",
        regions = "Nigeria, Benin, Togo",
        flagEmoji = "🇳🇬",
        description = "Rich tonal language known for musicality, royal court poetry, and the deep philosophy of Omoluabi.",
        toneOrScriptNote = "Tonal: High (´), Mid (unmarked), Low (`) tone changes meaning.",
        sampleProverb = "Ilé la ti ń kọ́ ẹ̀ṣọ́ ròde.",
        proverbTranslation = "Charity and good character begin at home."
    ),
    SWAHILI(
        id = "swahili",
        displayName = "Swahili",
        nativeName = "Kiswahili",
        greeting = "Habari yako!",
        regions = "Kenya, Tanzania, Uganda, DRC",
        flagEmoji = "🇰🇪",
        description = "East Africa's lingua franca and one of the world's most widely spoken Bantu languages.",
        toneOrScriptNote = "Non-tonal with regular penultimate stress and 16 noun classes.",
        sampleProverb = "Haraka haraka haina baraka.",
        proverbTranslation = "Hurry hurry has no blessing (Patience brings peace)."
    ),
    IGBO(
        id = "igbo",
        displayName = "Igbo",
        nativeName = "Ásụ̀sụ́ Ìgbò",
        greeting = "Kèdú kwanụ!",
        regions = "Southeastern Nigeria, Diaspora",
        flagEmoji = "🇳🇬",
        description = "Expressive Niger-Congo language celebrated for rich idiomatic proverbs and communal heritage.",
        toneOrScriptNote = "Tonal with high, low, and downstepped tones plus vowel harmony.",
        sampleProverb = "Ihe na-emetụ n'anya na-emetụ n'imi.",
        proverbTranslation = "What affects the eye also affects the nose (We are interconnected)."
    ),
    HAUSA(
        id = "hausa",
        displayName = "Hausa",
        nativeName = "Harshen Hausa",
        greeting = "Sannu da zuwa!",
        regions = "Northern Nigeria, Niger, Ghana",
        flagEmoji = "🇳🇬",
        description = "Leading trade and literary language of the Sahel with over 80 million energetic speakers.",
        toneOrScriptNote = "Tone & vowel length distinctions; written in Boko (Latin) and Ajami (Arabic).",
        sampleProverb = "Komi ya yi nisa, zai zo ƙasa.",
        proverbTranslation = "Whatever rises high must eventually return to earth."
    ),
    TWI(
        id = "twi",
        displayName = "Twi",
        nativeName = "Asante Twi",
        greeting = "Akwaaba!",
        regions = "Ghana, Ivory Coast",
        flagEmoji = "🇬🇭",
        description = "Spirited Akan language of the Ashanti kingdom, renowned for gold craftsmanship and day-names.",
        toneOrScriptNote = "Tonal pitch accents with vowel harmony (ATR advanced tongue root).",
        sampleProverb = "Baabuɔ panyin a, ɔbɛyɛ panyin.",
        proverbTranslation = "When you respect elders, you will grow to become one."
    ),
    AMHARIC(
        id = "amharic",
        displayName = "Amharic",
        nativeName = "አማርኛ (Amarəñña)",
        greeting = "Selam! (ሰላም)",
        regions = "Ethiopia, Horn of Africa",
        flagEmoji = "🇪🇹",
        description = "Ancient Semitic working language of Ethiopia written in the historic Ge'ez syllabary (Fidel).",
        toneOrScriptNote = "Fidel abugida script (each symbol combines consonant + vowel); non-tonal.",
        sampleProverb = "ድር ቢያብር አንበሳ ያስር።",
        proverbTranslation = "When spider webs unite, they can tie up a lion (Unity is strength)."
    ),
    ZULU(
        id = "zulu",
        displayName = "isiZulu",
        nativeName = "isiZulu",
        greeting = "Sawubona!",
        regions = "South Africa, Southern Africa",
        flagEmoji = "🇿🇦",
        description = "Major Nguni Bantu language celebrated for click consonants (c, q, x) and the profound philosophy of Ubuntu.",
        toneOrScriptNote = "Tonal with high and low tones; distinctive click consonants c, q, x.",
        sampleProverb = "Umuntu ngumuntu ngabantu.",
        proverbTranslation = "A person is a person through other people (Ubuntu)."
    )
}

enum class ProficiencyLevel(
    val code: String,
    val title: String,
    val subtitle: String,
    val estimatedLessons: Int
) {
    A1("A1", "Beginner", "Roots & Greetings", 12),
    A2("A2", "Elementary", "Market, Family & Daily Life", 16),
    B1("B1", "Intermediate", "Journeys, Work & Storytelling", 20),
    B2("B2", "Upper-Intermediate", "Debates & Cultural Nuances", 24),
    C1("C1", "Advanced", "Oratory, Proverbs & Literature", 28),
    C2("C2", "Mastery", "Deep Wisdom & Fluency", 32)
}

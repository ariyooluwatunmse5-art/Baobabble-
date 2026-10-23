package com.example.data.repository

import com.example.data.model.*

object LinguisticProfileRepository {

    fun getProfile(language: AfricanLanguage): LinguisticProfile {
        return when (language) {
            AfricanLanguage.YORUBA -> yorubaProfile
            AfricanLanguage.SWAHILI -> swahiliProfile
            AfricanLanguage.IGBO -> igboProfile
            AfricanLanguage.HAUSA -> hausaProfile
            AfricanLanguage.TWI -> twiProfile
            AfricanLanguage.AMHARIC -> amharicProfile
            AfricanLanguage.ZULU -> zuluProfile
        }
    }

    // -------------------------------------------------------------
    // YORUBA LINGUISTIC SYSTEM (AUTHENTIC & COMPREHENSIVE)
    // -------------------------------------------------------------
    private val yorubaProfile = LinguisticProfile(
        language = AfricanLanguage.YORUBA,
        alphabet = listOf(
            AlphabetLetter("A a", "[a]", "Open front unrounded vowel like 'f-a-ther'", "Adé", "Crown"),
            AlphabetLetter("B b", "[b]", "Voiced bilabial stop as in 'boy'", "Bàbá", "Father"),
            AlphabetLetter("D d", "[d]", "Alveolar stop as in 'day'", "Dàdá", "Dreadlocked child"),
            AlphabetLetter("E e", "[e]", "Close-mid front vowel like French 'été' or 'café'", "Eku", "Rat"),
            AlphabetLetter("Ẹ ẹ", "[ɛ]", "Open-mid vowel (sub-dot mark) like 'b-e-d'", "Ẹja", "Fish", true),
            AlphabetLetter("F f", "[f]", "Labiodental fricative as in 'fire'", "Fìlà", "Cap / Hat"),
            AlphabetLetter("G g", "[ɡ]", "Hard velar stop as in 'go'", "Gèlè", "Headwrap"),
            AlphabetLetter("Gb gb", "[ɡ͡b]", "Labial-velar stop. Articulated simultaneously with lips and back of tongue without a vowel between", "Gbogbo", "All / Everyone", true),
            AlphabetLetter("H h", "[h]", "Glottal fricative as in 'house'", "Hóhó", "Bare / Empty"),
            AlphabetLetter("I i", "[i]", "Close front vowel as in 'see'", "Igi", "Tree"),
            AlphabetLetter("J j", "[d͡ʒ]", "Voiced postalveolar affricate as in 'judge'", "Jẹun", "Eat food"),
            AlphabetLetter("K k", "[k]", "Voiceless velar stop as in 'king'", "Kọ́kọ́rọ́", "Key"),
            AlphabetLetter("L l", "[l]", "Alveolar lateral approximant as in 'love'", "Lágbájá", "Somebody"),
            AlphabetLetter("M m", "[m]", "Bilabial nasal as in 'mother'", "Màmá", "Mother"),
            AlphabetLetter("N n", "[n]", "Alveolar nasal as in 'name'", "Nkan", "Something"),
            AlphabetLetter("O o", "[o]", "Close-mid back vowel as in 'go' without diphthong", "Omi", "Water"),
            AlphabetLetter("Ọ ọ", "[ɔ]", "Open-mid back vowel (sub-dot mark) like 'th-ou-ght' or 'pot'", "Ọ̀rọ̀", "Word / Speech", true),
            AlphabetLetter("P p", "[k͡p]", "Voiceless labial-velar stop (NOT English P). Pronounce 'k' and 'p' simultaneously with lips and tongue", "Púpọ̀", "Many / Very", true),
            AlphabetLetter("R r", "[ɾ]", "Alveolar tap/flap, gently rolled as in Spanish 'pero'", "Rárá", "No / Never"),
            AlphabetLetter("S s", "[s]", "Voiceless alveolar fricative as in 'sun'", "Sùn", "Sleep"),
            AlphabetLetter("Ṣ ṣ", "[ʃ]", "Voiceless postalveolar fricative (sub-dot mark) like 'sh-ip'", "Ṣe", "Do / Make", true),
            AlphabetLetter("T t", "[t]", "Voiceless alveolar stop as in 'table'", "Ta", "Sell"),
            AlphabetLetter("U u", "[u]", "Close back rounded vowel like 'fl-oo-d' / 'boot'", "Ugbó", "Forest (dialect)"),
            AlphabetLetter("W w", "[w]", "Labial-velar approximant as in 'water'", "Wá", "Come"),
            AlphabetLetter("Y y", "[j]", "Palatal approximant as in 'yellow'", "Yọ̀", "Rejoice")
        ),
        toneSystem = listOf(
            ToneDetail(
                name = "High Tone (Àmì Òkè)",
                diacriticSymbol = "´ (acute accent: á, é, ẹ́, í, ó, ọ́, ú)",
                solfaNote = "Mí",
                pitchDescription = "High, sharp, musical peak pitch",
                audioKey = "mi_high",
                exampleNative = "Wá (Come), Rí (See)",
                exampleMeaning = "Rising or high melodic note"
            ),
            ToneDetail(
                name = "Mid Tone (Àmì Àárín)",
                diacriticSymbol = "Unmarked (a, e, ẹ, i, o, ọ, u)",
                solfaNote = "Re",
                pitchDescription = "Neutral baseline resting speaking pitch",
                audioKey = "re_mid",
                exampleNative = "Je (Eat), Lo (Go)",
                exampleMeaning = "Neutral baseline pitch"
            ),
            ToneDetail(
                name = "Low Tone (Àmì Ìsàlẹ̀)",
                diacriticSymbol = "` (grave accent: à, è, ẹ̀, ì, ò, ọ̀, ù)",
                solfaNote = "Dò",
                pitchDescription = "Deep, resonant, downward falling pitch",
                audioKey = "do_low",
                exampleNative = "Lọ̀ (Grind), Rù (Carry)",
                exampleMeaning = "Deep grounded melodic note"
            )
        ),
        minimalToneSets = listOf(
            MinimalToneSet(
                baseLetters = "ọ - k - ọ",
                explanation = "One of the most famous tonal triplets in Yorùbá linguistics. Identical consonants and vowels, completely distinct meanings purely determined by tones:",
                variations = listOf(
                    ToneVariation("Ọkọ", "Mid - Mid", "Re - Re", "Husband", "Ọkọ mi ni (He is my husband)."),
                    ToneVariation("Ọkọ̀", "Mid - Low", "Re - Dò", "Vehicle / Car / Boat", "Wọ ọkọ̀ lọ sí Èkó (Board the vehicle to Lagos)."),
                    ToneVariation("Ọkọ́", "Mid - High", "Re - Mí", "Hoe / Farming tool", "Bàbá fi ọkọ́ ro oko (Father cultivated the farm with a hoe)."),
                    ToneVariation("Ọ̀kọ̀", "Low - Low", "Dò - Dò", "Spear / Javelin", "Ọ̀kọ̀ ogun ti àwọn jagunjagun (The warriors' spear).")
                )
            ),
            MinimalToneSet(
                baseLetters = "i - g - b - a",
                explanation = "Tones define fundamental concepts of life, counting, time, and nature:",
                variations = listOf(
                    ToneVariation("Igbá", "Mid - High", "Re - Mí", "Calabash (traditional gourd vessel)", "Fi omi sínú igbá (Put water inside the calabash)."),
                    ToneVariation("Igba", "Mid - Mid", "Re - Re", "Two hundred (200)", "Igba náírà péré (Only two hundred Naira)."),
                    ToneVariation("Ìgbà", "Low - Low", "Dò - Dò", "Time / Season / Era", "Ní ìgbà àtijọ́ (In times of old)."),
                    ToneVariation("Igbó", "Mid - High", "Re - Mí", "Forest / Dense woods", "Igbó kìjikìji (A vast dense forest).")
                )
            )
        ),
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Morning Greeting (Dawn to 12 PM)",
                nativePhrase = "Ẹ kú àárọ̀ (Honorific) / O kú àárọ̀ (Peer)",
                literalTranslation = "Greetings on surviving/witnessing the morning",
                englishMeaning = "Good morning",
                politeResponse = "Àárọ̀ rere o / Àlàáfíà ni",
                culturalEtiquette = "Men perform Ìdọ̀bálẹ̀ (prostration) or bow chest forward; women perform Ìkúnlẹ̀ (kneeling on knees)."
            ),
            CulturalContextGreeting(
                situation = "Afternoon Greeting (12 PM to 4 PM)",
                nativePhrase = "Ẹ kú ọ̀sán",
                literalTranslation = "Greetings on the afternoon sun",
                englishMeaning = "Good afternoon",
                politeResponse = "Ọ̀sán rere o",
                culturalEtiquette = "Acknowledge the heat of the day and ask after work."
            ),
            CulturalContextGreeting(
                situation = "Evening Greeting (4 PM to 7 PM)",
                nativePhrase = "Ẹ kú ìrọ̀lẹ́",
                literalTranslation = "Greetings on the gentle twilight hour",
                englishMeaning = "Good early evening",
                politeResponse = "Ìrọ̀lẹ́ rere o",
                culturalEtiquette = "Transition period when folks return from farm, stalls, or workplace."
            ),
            CulturalContextGreeting(
                situation = "Night Greeting (After dark to bedtime)",
                nativePhrase = "Ẹ kú alẹ́ / O d'àárọ̀",
                literalTranslation = "Greetings on the night / Until the dawn",
                englishMeaning = "Good evening / Goodnight",
                politeResponse = "Alẹ́ rere o / K'Ọ́lọ́run jí wa dáadáa (May God awaken us well)",
                culturalEtiquette = "Spoken with prayers for safe sleep and morning peace."
            ),
            CulturalContextGreeting(
                situation = "Greeting someone working or in business",
                nativePhrase = "Ẹ kú iṣẹ́",
                literalTranslation = "Greetings on your labor and exertion",
                englishMeaning = "Well done with your work",
                politeResponse = "Ẹ ṣeé púpọ̀ / Ẹ jẹ́ ká ríre",
                culturalEtiquette = "Never pass someone working without honoring their toil with 'Ẹ kú iṣẹ́'!"
            ),
            CulturalContextGreeting(
                situation = "Congratulating on escaping danger, surviving illness, or childbirth",
                nativePhrase = "Ẹ kú ewu",
                literalTranslation = "Greetings on having conquered danger",
                englishMeaning = "Congratulations on safety / Safe delivery",
                politeResponse = "Ewu kò ní kọ́ wa mọ́ (Danger shall not cross our path again)",
                culturalEtiquette = "Extremely emotional, communal solidarity phrase deeply rooted in resilience."
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "yo_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "Basic Sentence Structure (S-V-O)",
                ruleSummary = "Yorùbá uses Subject + Verb + Object. Unlike European languages, Yorùbá verbs do NOT conjugate for person or number. The verb root stays identical!",
                formulaPattern = "[Subject Pronoun] + [Verb] + [Object]",
                examples = listOf(
                    GrammarExample("Mo jẹ isu.", "Mo (I) + jẹ (eat) + isu (yam).", "I ate yam / I eat yam."),
                    GrammarExample("A mu omi.", "A (We) + mu (drink) + omi (water).", "We drank water."),
                    GrammarExample("Wọ́n ra aṣọ.", "Wọ́n (They/Elder) + ra (buy) + aṣọ (cloth).", "They bought cloth.")
                ),
                commonMistake = "Do NOT add endings to the verb. 'Mo jẹ', 'O jẹ', 'A jẹ' all use the exact same root 'jẹ'!"
            ),
            GrammarRule(
                id = "yo_g2",
                cefrLevel = ProficiencyLevel.A1,
                title = "Tense & Aspect Markers: Continuous (ń), Past (ti), Future (máa)",
                ruleSummary = "Tenses are expressed by placing invariant auxiliary particles directly before the verb root.",
                formulaPattern = "[Subject] + [ń / ti / máa] + [Verb]",
                examples = listOf(
                    GrammarExample("Mo ń kọ́ èdè Yorùbá.", "Mo (I) + ń (continuous -ing) + kọ́ (learn) + èdè Yorùbá.", "I am learning the Yoruba language."),
                    GrammarExample("Mo ti jẹun.", "Mo (I) + ti (perfective completed) + jẹun (eat food).", "I have already eaten."),
                    GrammarExample("Màá lọ sí ọjà.", "Màá (Mo + máa contraction) + lọ (go) + sí (to) + ọjà (market).", "I will go to the market.")
                ),
                commonMistake = "Don't confuse 'ń' (high tone continuous marker) with the pronoun 'n' (I) in negative constructions."
            ),
            GrammarRule(
                id = "yo_g3",
                cefrLevel = ProficiencyLevel.A2,
                title = "Honorific Respect Pronouns (Ẹ & Wọ́n)",
                ruleSummary = "In Yorùbá, using singular 'O' (you) or 'Ó' (he/she) for an elder, parent, or stranger is considered a serious cultural transgression. You MUST use 'Ẹ' (plural You) and 'Wọ́n' (plural They) even when speaking to one single elder.",
                formulaPattern = "Ẹ + [Greeting/Verb] (to elders); O + [Greeting/Verb] (to peers)",
                examples = listOf(
                    GrammarExample("Ẹ kú àárọ̀, bàbá.", "Ẹ (Honorific You) + kú àárọ̀ (good morning) + bàbá (father).", "Good morning, father (Respectful)."),
                    GrammarExample("Bàbá ti dé; wọ́n ń sùn.", "Bàbá (Father) + ti dé (arrived); + wọ́n (They - honorific) + ń sùn (sleeping).", "Father has arrived; he (respectfully) is sleeping.")
                ),
                commonMistake = "Calling your mother or an elder 'O' instead of 'Ẹ'."
            ),
            GrammarRule(
                id = "yo_g4",
                cefrLevel = ProficiencyLevel.B1,
                title = "Negative Sentences with Kò / Kò Ní / Kì Í",
                ruleSummary = "Negation is created with particles placed before the verb: 'kò' for general negation, 'kò ní' for negative future, and 'kì í' for habitual negative ('does not usually').",
                formulaPattern = "[Subject] + [kò / kì í] + [Verb]",
                examples = listOf(
                    GrammarExample("Mi ò mọ̀ (Mo kò mọ̀).", "Mo (I) + kò (negation) + mọ̀ (know).", "I do not know."),
                    GrammarExample("Kò ní wá lónìí.", "Ó (He/She) + kò ní (negative future) + wá (come) + lónìí (today).", "He/She will not come today."),
                    GrammarExample("Ọmọlúwàbí kì í purọ́.", "Ọmọlúwàbí (Noble person) + kì í (habitually never) + purọ́ (lie).", "A person of noble character never lies.")
                ),
                commonMistake = "'Mo kò' frequently contracts in speech to 'Mi ò'."
            ),
            GrammarRule(
                id = "yo_g5",
                cefrLevel = ProficiencyLevel.C1,
                title = "Nominalization (Converting Verbs to Nouns)",
                ruleSummary = "Yorùbá forms countless nouns by prefixing vowels (A-, E-, È-, Ò-, Ọ-) or the agentive prefix 'Olù-' or 'Oní-' (owner/doer) to verb stems.",
                formulaPattern = "Oní- / Olù- + [Root Verb/Noun]",
                examples = listOf(
                    GrammarExample("Kọ́ (to teach) → Olùkọ́ (teacher)", "Olù + kọ́ (agentive + teach)", "A teacher / instructor"),
                    GrammarExample("Oko (farm) → Àgbẹ̀ / Oníko", "A + gbé (dweller) or Oní + oko (owner of farm)", "Farmer"),
                    GrammarExample("Ọjà (market) → Ọlọ́jà", "Oní + ọjà (owner/leader of market)", "Market trader / Vendor")
                ),
                commonMistake = "Vowel assimilation turns 'Oní' into 'Ọlọ́-' before open 'ọ' (e.g. Oní + ọjà = Ọlọ́jà)."
            )
        ),
        culturalPhilosophyTitle = "The Philosophy of Ọmọlúwàbí (Noble Character)",
        culturalPhilosophyText = "In Yorùbá worldview, language is not mere grammar; it is the mirror of character. An Ọmọlúwàbí (Ọmọ tí Olú-ìwà bí - 'A child born of the source of good character') embodies humility, truthfulness, respectful speech (Ọ̀wọ̀), mastery of proverbs (Òwe), and selfless communal solidarity. Every greeting is an act of blessing and mutual care.",
        aiTutorLinguisticDirectives = """
            STRICT YORUBA LINGUISTIC RULES FOR AI TUTOR:
            1. Always write Yoruba words with correct tone diacritics: High (´), Low (`), Mid (unmarked), and sub-dots on Ẹ (ẹ), Ọ (ọ), and Ṣ (ṣ).
            2. Never invent words or mix Yoruba with other languages. If you don't know a rare idiom, admit it with modesty.
            3. Respect CEFR Level:
               - A1: Short, simple SVO sentences, basic morning/afternoon greetings, numerals 1-20, essential food and family words.
               - A2: Market bargaining ('Élò ni?', 'Dín owó kù'), time of day, continuous aspect ('ń').
               - B1/B2: Kinship titles, storytelling, negations with 'kò' and 'kì í', travel around Lagos/Ibadan.
               - C1/C2: Traditional proverbs (Òwe), nominalization (Olùkọ́, Ọlọ́jà), praise poetry (Oríkì) and Ọmọlúwàbí philosophy.
            4. In every correction, output:
               • Correct form with tones
               • Solfa melodic pitch (e.g. Dò-Re-Mí)
               • Literal meaning
               • Respectful cultural context note.
        """.trimIndent()
    )

    // -------------------------------------------------------------
    // SWAHILI LINGUISTIC SYSTEM
    // -------------------------------------------------------------
    private val swahiliProfile = LinguisticProfile(
        language = AfricanLanguage.SWAHILI,
        alphabet = listOf(
            AlphabetLetter("A a", "[a]", "Open vowel like 'father'", "Asante", "Thank you"),
            AlphabetLetter("B b", "[ɓ]", "Voiced bilabial implosive/stop", "Bora", "Better"),
            AlphabetLetter("Ch ch", "[t͡ʃ]", "Voiceless affricate like 'chair'", "Chakula", "Food"),
            AlphabetLetter("D d", "[ɗ]", "Alveolar stop/implosive", "Dada", "Sister"),
            AlphabetLetter("Dh dh", "[ð]", "Voiced dental fricative like English 'th-is'", "Dhahabu", "Gold", true),
            AlphabetLetter("E e", "[ɛ]", "Mid front vowel like 'b-e-d'", "Elimu", "Education"),
            AlphabetLetter("F f", "[f]", "Labiodental fricative", "Familia", "Family"),
            AlphabetLetter("G g", "[ɠ]", "Hard velar stop like 'go'", "Gari", "Car"),
            AlphabetLetter("Gh gh", "[ɣ]", "Voiced velar fricative (Arabic origin) like Parisian French 'r'", "Ghafla", "Suddenly", true),
            AlphabetLetter("H h", "[h]", "Glottal fricative", "Habari", "News / How are you"),
            AlphabetLetter("I i", "[i]", "Close front vowel as in 'see'", "Imara", "Strong"),
            AlphabetLetter("J j", "[ɟ]", "Voiced palatal stop like 'jump'", "Jambo", "Matter / Hello"),
            AlphabetLetter("K k", "[k]", "Voiceless velar stop", "Karibu", "Welcome"),
            AlphabetLetter("Kh kh", "[x]", "Voiceless velar fricative like Scottish 'loch'", "Kheri", "Blessing / Goodness", true),
            AlphabetLetter("L l", "[l]", "Alveolar lateral", "Lugha", "Language"),
            AlphabetLetter("M m", "[m]", "Bilabial nasal (can be syllabic: M-tu)", "Mwalimu", "Teacher"),
            AlphabetLetter("N n", "[n]", "Alveolar nasal", "Ndege", "Bird / Airplane"),
            AlphabetLetter("Ng' ng'", "[ŋ]", "Velar nasal like 'si-ng-er' (distinct from ng in finger)", "Ng'ombe", "Cow", true),
            AlphabetLetter("Ny ny", "[ɲ]", "Palatal nasal like Spanish 'ñ' or 'canyon'", "Nyumba", "House"),
            AlphabetLetter("O o", "[ɔ]", "Open-mid back vowel like 'pot'", "Ondoka", "Depart"),
            AlphabetLetter("P p", "[p]", "Voiceless bilabial stop", "Paka", "Cat"),
            AlphabetLetter("R r", "[r]", "Alveolar trill / tap", "Rafiki", "Friend"),
            AlphabetLetter("S s", "[s]", "Voiceless alveolar fricative", "Simba", "Lion"),
            AlphabetLetter("Sh sh", "[ʃ]", "Voiceless postalveolar like 'shine'", "Shukrani", "Gratitude"),
            AlphabetLetter("T t", "[t]", "Voiceless alveolar stop", "Tafadhali", "Please"),
            AlphabetLetter("Th th", "[θ]", "Voiceless dental fricative like 'th-ink'", "Thamani", "Value", true),
            AlphabetLetter("U u", "[u]", "Close back rounded vowel like 'boot'", "Uhuru", "Freedom / Independence"),
            AlphabetLetter("V v", "[v]", "Voiced labiodental fricative", "Viatu", "Shoes"),
            AlphabetLetter("W w", "[w]", "Labial-velar approximant", "Watu", "People"),
            AlphabetLetter("Y y", "[j]", "Palatal approximant", "Yai", "Egg"),
            AlphabetLetter("Z z", "[z]", "Voiced alveolar fricative", "Zuri", "Beautiful / Good")
        ),
        toneSystem = listOf(
            ToneDetail(
                name = "Penultimate Stress (Regular)",
                diacriticSymbol = "Non-tonal; stressed on 2nd-to-last syllable",
                solfaNote = "Rhythmic Stress",
                pitchDescription = "Nearly every Swahili word places primary vocal stress on the second-to-last syllable",
                audioKey = "penultimate_stress",
                exampleNative = "Ka-RI-bu (Welcome), A-SAN-te (Thank you)",
                exampleMeaning = "Predictable, rhythmic cadence"
            )
        ),
        minimalToneSets = emptyList(), // Swahili is non-tonal; stress distinguishes loanwords
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Universal Greeting to peers/tourists",
                nativePhrase = "Hujambo? (Singular) / Hamjambo? (Plural)",
                literalTranslation = "Do you have no problems/matters?",
                englishMeaning = "Hello / How are you?",
                politeResponse = "Sijambo (I have no troubles) / Hatujambo (We are well)",
                culturalEtiquette = "Universal polite inquiry."
            ),
            CulturalContextGreeting(
                situation = "Respectful greeting strictly reserved for elders and teachers",
                nativePhrase = "Shikamoo (Shikamo)",
                literalTranslation = "Nashika miguu yako (I hold your feet with deep humility)",
                englishMeaning = "My respectful greetings to you, elder",
                politeResponse = "Marahaba (I accept your respect with joy and blessing)",
                culturalEtiquette = "Essential etiquette across Tanzania and Kenya. Never greet an elder with casual 'Jambo'!"
            ),
            CulturalContextGreeting(
                situation = "Asking for news of the day",
                nativePhrase = "Habari za asubuhi? / Habari za kazi?",
                literalTranslation = "News of the morning? / News of the work?",
                englishMeaning = "Good morning / How is work going?",
                politeResponse = "Nzuri sana / Salama kabisa (Very good / Peaceful)",
                culturalEtiquette = "In Swahili coast culture, asking for news establishes community well-being before commerce."
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "sw_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "16 Noun Classes (M/Wa, Ki/Vi, M/Mi)",
                ruleSummary = "Swahili nouns are divided into 16 agreement classes. Class 1/2 are human beings: prefix M- for singular, Wa- for plural. Adjectives and verbs MUST agree with these prefixes!",
                formulaPattern = "M- + [Stem] (sing.) → Wa- + [Stem] (plur.)",
                examples = listOf(
                    GrammarExample("Mtu mzuri / Watu wazuri", "M-tu m-zuri / Wa-tu wa-zuri", "A good person / Good people"),
                    GrammarExample("Mtoto anacheza.", "M-toto (child) + a- (he/she) + na- (present) + cheza (play).", "The child is playing."),
                    GrammarExample("Watoto wanacheza.", "Wa-toto (children) + wa- (they) + na- (present) + cheza (play).", "The children are playing.")
                ),
                commonMistake = "Forgetting adjective agreement: 'Mtu mzuri', not 'Mtu zuri'."
            ),
            GrammarRule(
                id = "sw_g2",
                cefrLevel = ProficiencyLevel.A1,
                title = "Agglutinative Verbs: Subject + Tense + Root",
                ruleSummary = "A single Swahili word functions as a complete sentence! It joins Subject prefix + Tense marker + Verb root.",
                formulaPattern = "[Ni/U/A/Tu/M/Wa] + [na/li/ta/me] + [Verb]",
                examples = listOf(
                    GrammarExample("Ninasoma.", "Ni (I) + na (present -ing) + soma (read).", "I am reading."),
                    GrammarExample("Nilisoma.", "Ni (I) + li (past) + soma (read).", "I read / I studied."),
                    GrammarExample("Nitasoma.", "Ni (I) + ta (future) + soma (read).", "I will read / I will study.")
                ),
                commonMistake = "Do not add separate English pronouns like 'Mimi ninasoma' unless emphasizing 'I, specifically'."
            )
        ),
        culturalPhilosophyTitle = "Umoja & Harambee (Unity & Pulling Together)",
        culturalPhilosophyText = "Swahili culture blends African Bantu warmth with Indian Ocean maritime heritage. Values of 'Umoja' (unity), 'Harambee' (all pulling together), and 'Utulivu' (serenity) infuse everyday conversation.",
        aiTutorLinguisticDirectives = """
            STRICT SWAHILI LINGUISTIC RULES FOR AI TUTOR:
            1. Enforce penultimate stress: Ka-RI-bu, A-SAN-te, ha-BA-ri.
            2. Enforce Noun Class agreement (M/Wa, Ki/Vi, etc.).
            3. Teach cultural elder greeting 'Shikamoo' -> 'Marahaba'.
            4. Explain agglutinative verb structure clearly with Subject + Tense + Verb breakdown.
        """.trimIndent()
    )

    // Profiles for Igbo, Hausa, Twi, Amharic
    private val igboProfile = LinguisticProfile(
        language = AfricanLanguage.IGBO,
        alphabet = listOf(
            AlphabetLetter("A a", "[a]", "Open front vowel", "Aka", "Hand"),
            AlphabetLetter("B b", "[b]", "Voiced bilabial", "Bata", "Enter"),
            AlphabetLetter("Ch ch", "[t͡ʃ]", "Postalveolar affricate", "Chineke", "God / Creator"),
            AlphabetLetter("D d", "[d]", "Alveolar stop", "Dàálụ", "Thank you"),
            AlphabetLetter("E e", "[e]", "Light vowel", "Ego", "Money"),
            AlphabetLetter("F f", "[f]", "Labiodental fricative", "Fefe", "Fly"),
            AlphabetLetter("G g", "[ɡ]", "Velar stop", "Gini", "What"),
            AlphabetLetter("Gb gb", "[ɡ͡b]", "Labial-velar stop", "Gbaghara", "Forgive", true),
            AlphabetLetter("Gh gh", "[ɣ]", "Velar fricative", "Ghara", "Leave"),
            AlphabetLetter("H h", "[h]", "Glottal fricative", "Hụ", "See"),
            AlphabetLetter("I i", "[i]", "Heavy close vowel", "Iko", "Cup"),
            AlphabetLetter("Ị ị", "[ɪ]", "Light vowel (sub-dot)", "Ịtẹ", "Pot", true),
            AlphabetLetter("J j", "[d͡ʒ]", "Affricate", "Jiri", "Hold"),
            AlphabetLetter("K k", "[k]", "Velar stop", "Kèdú", "How / Hello"),
            AlphabetLetter("Kp kp", "[k͡p]", "Labial-velar voiceless stop", "Kpaa", "Discuss", true),
            AlphabetLetter("Kw kw", "[kʷ]", "Labialized velar stop", "Kwuo", "Speak", true),
            AlphabetLetter("L l", "[l]", "Lateral", "Lota", "Remember"),
            AlphabetLetter("M m", "[m]", "Bilabial nasal", "Mmiri", "Water"),
            AlphabetLetter("N n", "[n]", "Alveolar nasal", "Nna", "Father"),
            AlphabetLetter("Nw nw", "[ŋʷ]", "Labialized nasal", "Nwa", "Child", true),
            AlphabetLetter("Ny ny", "[ɲ]", "Palatal nasal", "Nye", "Give", true),
            AlphabetLetter("O o", "[o]", "Heavy close-mid vowel", "Oche", "Chair"),
            AlphabetLetter("Ọ ọ", "[ɔ]", "Light open-mid vowel (sub-dot)", "Ọkụkọ", "Chicken", true),
            AlphabetLetter("P p", "[p]", "Voiceless bilabial", "Pụọ", "Leave"),
            AlphabetLetter("R r", "[r]", "Alveolar trill", "Rie", "Eat"),
            AlphabetLetter("S s", "[s]", "Alveolar fricative", "Soro", "Follow"),
            AlphabetLetter("Sh sh", "[ʃ]", "Postalveolar fricative", "Shị", "Defecate / Odor"),
            AlphabetLetter("T t", "[t]", "Alveolar stop", "Tata", "Today"),
            AlphabetLetter("U u", "[u]", "Heavy close back vowel", "Ugo", "Eagle"),
            AlphabetLetter("Ụ ụ", "[ʊ]", "Light near-close vowel (sub-dot)", "Ụlọ", "House", true),
            AlphabetLetter("V v", "[v]", "Labiodental fricative", "Vuta", "Bring"),
            AlphabetLetter("W w", "[w]", "Approximant", "Wepụ", "Take away"),
            AlphabetLetter("Y y", "[j]", "Palatal approximant", "Yiri", "Resemble"),
            AlphabetLetter("Z z", "[z]", "Alveolar fricative", "Zụọ", "Buy")
        ),
        toneSystem = listOf(
            ToneDetail("High Tone", "´ (Ó)", "Mí", "Sharp high pitch", "ig_high", "Ákwá", "Crying"),
            ToneDetail("Low Tone", "` (Ò)", "Dò", "Deep low pitch", "ig_low", "Àkwà", "Bed / Cloth"),
            ToneDetail("Downstep", "¯ (Ō)", "Re", "Slight pitch descent following high tone", "ig_down", "Ákwā", "Egg")
        ),
        minimalToneSets = listOf(
            MinimalToneSet(
                baseLetters = "a - k - w - a",
                explanation = "Famous Igbo tone quadruplet with completely distinct meanings based strictly on tone:",
                variations = listOf(
                    ToneVariation("Ákwá", "High - High", "Mí - Mí", "Crying / Tears", "Nwa na-akwa ákwá (The baby is crying)."),
                    ToneVariation("Àkwà", "Low - Low", "Dò - Dò", "Bed / Bridge", "Dina n'elu àkwà (Lie down on the bed)."),
                    ToneVariation("Ákwà", "High - Low", "Mí - Dò", "Cloth / Fabric", "Akwa mara mma (Beautiful cloth)."),
                    ToneVariation("Ákwā", "High - Downstep", "Mí - Re", "Egg", "Nri ákwā (Egg meal).")
                )
            )
        ),
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Universal Greeting",
                nativePhrase = "Kèdú? / Kèdú kwanụ?",
                literalTranslation = "How is it? / How are you doing?",
                englishMeaning = "Hello / How are you?",
                politeResponse = "Ọ dị mma / A nọ m nke ọma (It is well / I am fine)",
                culturalEtiquette = "Can be used with youth and elders alike."
            ),
            CulturalContextGreeting(
                situation = "Morning Greeting",
                nativePhrase = "Ụtụtụ ọma / Ị bọọla chi?",
                literalTranslation = "Good morning / Did you witness the daybreak well?",
                englishMeaning = "Good morning",
                politeResponse = "Ụtụtụ ọma / Èeh, a bọọla m chi",
                culturalEtiquette = "Deep inquiry into surviving the night."
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "ig_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "Vowel Harmony (Light vs Heavy Groups)",
                ruleSummary = "Igbo vowels are divided into Light (dotted: ị, ọ, ụ, a) and Heavy (undotted: i, o, u, e). Within a root word, all vowels must belong to the same harmony set!",
                formulaPattern = "Group A (Light: a, ị, ọ, ụ) vs Group B (Heavy: e, i, o, u)",
                examples = listOf(
                    GrammarExample("Ụlọ (house) - all light vowels", "Ụ (light) + L + Ọ (light)", "House"),
                    GrammarExample("Ego (money) - all heavy vowels", "E (heavy) + G + O (heavy)", "Money")
                ),
                commonMistake = "Mixing light and heavy vowels in verb prefixes."
            )
        ),
        culturalPhilosophyTitle = "Ọjị & Ịgọ Ọfọ (The Sacred Kola Nut & Justice)",
        culturalPhilosophyText = "In Igbo philosophy: 'Onye wetara ọjị wetara ndụ' (He who brings kola nut brings life). The kola nut is broken only after prayers of truth and justice (Ịgọ Ọfọ).",
        aiTutorLinguisticDirectives = "Enforce Igbo vowel harmony, downstepped tones, and respect in greetings."
    )

    private val hausaProfile = LinguisticProfile(
        language = AfricanLanguage.HAUSA,
        alphabet = listOf(
            AlphabetLetter("A a", "[a]", "Vowel", "Abinci", "Food"),
            AlphabetLetter("B b", "[b]", "Voiced bilabial", "Babba", "Big / Elder"),
            AlphabetLetter("Ɓ ɓ", "[ɓ]", "Glottalized/implosive B (hooked)", "Ɓarawo", "Thief", true),
            AlphabetLetter("C c", "[t͡ʃ]", "Voiceless affricate like 'chair'", "Ciki", "Inside / Stomach"),
            AlphabetLetter("D d", "[d]", "Alveolar stop", "Daki", "Room"),
            AlphabetLetter("Ɗ ɗ", "[ɗ]", "Glottalized/implosive D (hooked)", "Ɗa", "Child / Son", true),
            AlphabetLetter("E e", "[e]", "Mid front vowel", "Enuwa", "Shade"),
            AlphabetLetter("F f", "[ɸ]", "Bilabial fricative (lips together)", "Fari", "White / Drought"),
            AlphabetLetter("G g", "[ɡ]", "Hard stop", "Gida", "House / Home"),
            AlphabetLetter("H h", "[h]", "Glottal", "Haske", "Light"),
            AlphabetLetter("I i", "[i]", "Vowel", "Iko", "Authority"),
            AlphabetLetter("J j", "[d͡ʒ]", "Affricate", "Ji", "Hear"),
            AlphabetLetter("K k", "[k]", "Stop", "Kifi", "Fish"),
            AlphabetLetter("Ƙ ƙ", "[kʼ]", "Glottalized/ejective K (hooked)", "Ƙasa", "Land / Country", true),
            AlphabetLetter("L l", "[l]", "Lateral", "Lafiya", "Health / Peace"),
            AlphabetLetter("M m", "[m]", "Nasal", "Mota", "Car"),
            AlphabetLetter("N n", "[n]", "Nasal", "Nama", "Meat"),
            AlphabetLetter("O o", "[o]", "Vowel", "Oga", "Boss"),
            AlphabetLetter("R r", "[r]", "Rolled/tapped R", "Rana", "Sun / Day"),
            AlphabetLetter("S s", "[s]", "Fricative", "Sannu", "Hello / Greetings"),
            AlphabetLetter("Sh sh", "[ʃ]", "Postalveolar", "Shayi", "Tea"),
            AlphabetLetter("T t", "[t]", "Stop", "Tafiya", "Journey"),
            AlphabetLetter("Ts ts", "[sʼ]", "Ejective alveolar affricate", "Tsaye", "Standing", true),
            AlphabetLetter("U u", "[u]", "Vowel", "Uba", "Father"),
            AlphabetLetter("W w", "[w]", "Approximant", "Wuta", "Fire"),
            AlphabetLetter("Y y", "[j]", "Approximant", "Yaro", "Boy"),
            AlphabetLetter("Ƴ ƴ", "[ʔʲ]", "Glottalized palatal Y (hooked)", "Ƴa", "Daughter / Children", true),
            AlphabetLetter("Z z", "[z]", "Fricative", "Zaki", "Lion")
        ),
        toneSystem = listOf(
            ToneDetail("High Tone", "Unmarked", "Mí", "High tone", "ha_high", "Fari", "White"),
            ToneDetail("Low Tone", "` (Grave)", "Dò", "Low tone", "ha_low", "Fàri", "Drought"),
            ToneDetail("Falling Tone", "^ (Circumflex)", "Mí-Dò", "High falling into low", "ha_fall", "Môtà", "Car")
        ),
        minimalToneSets = emptyList(),
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Universal Greeting",
                nativePhrase = "Sannu / Sannu da zuwa",
                literalTranslation = "Gentleness / Greetings on your arrival",
                englishMeaning = "Hello / Welcome",
                politeResponse = "Yauwa, sannu kadai",
                culturalEtiquette = "Accompanied by touching right hand to heart."
            ),
            CulturalContextGreeting(
                situation = "Morning Greeting",
                nativePhrase = "Ina kwana? / Ina kwana lafiya?",
                literalTranslation = "Where/how was your sleeping? All in peace?",
                englishMeaning = "Good morning",
                politeResponse = "Lafiya lau / Alhamdu lillahi (All peace / Praise God)",
                culturalEtiquette = "Inquire after household health ('Ina gida?')."
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "ha_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "Grammatical Gender (Masculine & Feminine)",
                ruleSummary = "Hausa nouns have gender. Feminine nouns usually end in '-a' (e.g. mota = car), while masculine nouns end in other vowels or consonants.",
                formulaPattern = "Nouns ending in -a are mostly feminine; others masculine",
                examples = listOf(
                    GrammarExample("Yaro (boy - masc.) / Yarinya (girl - fem.)", "Yaro / Yarinya", "Boy / Girl"),
                    GrammarExample("Wannan mota tawa ce.", "Wannan (this) + mota (car - fem.) + tawa ce (is mine).", "This is my car.")
                ),
                commonMistake = "Using masculine agreement particle 'ne' instead of feminine 'ce'."
            )
        ),
        culturalPhilosophyTitle = "Lafiya & Zumunci (Health, Peace & Kinship Fellowship)",
        culturalPhilosophyText = "In Hausa society, 'Lafiya' (peace and wholeness) and 'Zumunci' (cherished family relations) are the bedrock of communal existence.",
        aiTutorLinguisticDirectives = "Enforce hooked glottal letters (ɓ, ɗ, ƙ, ƴ) and gender agreement (ne vs ce)."
    )

    private val twiProfile = LinguisticProfile(
        language = AfricanLanguage.TWI,
        alphabet = listOf(
            AlphabetLetter("A a", "[a]", "Open vowel", "Akwaaba", "Welcome"),
            AlphabetLetter("B b", "[b]", "Bilabial stop", "Bɔkɔɔ", "Cool / Peaceful"),
            AlphabetLetter("D d", "[d]", "Alveolar stop", "Daase", "Thanks"),
            AlphabetLetter("E e", "[e]", "Close-mid vowel", "Edan", "House"),
            AlphabetLetter("Ɛ ɛ", "[ɛ]", "Open-mid vowel (open E)", "Ɛyɛ", "It is good", true),
            AlphabetLetter("F f", "[f]", "Fricative", "Fie", "Home"),
            AlphabetLetter("G g", "[ɡ]", "Velar stop", "Gyina", "Stand"),
            AlphabetLetter("H h", "[h]", "Glottal", "Hye", "Burn / Wear"),
            AlphabetLetter("I i", "[i]", "Vowel", "Inu", "Eye"),
            AlphabetLetter("K k", "[k]", "Stop", "Kofi", "Friday-born male"),
            AlphabetLetter("Kw kw", "[kʷ]", "Labialized stop", "Kwame", "Saturday-born male", true),
            AlphabetLetter("Ky ky", "[t͡ɕ]", "Palatalized affricate like 'church'", "Kyɛw", "Hat / Excuse me", true),
            AlphabetLetter("L l", "[l]", "Lateral", "Lorry", "Bus"),
            AlphabetLetter("M m", "[m]", "Nasal", "Me", "I / Me / My"),
            AlphabetLetter("N n", "[n]", "Nasal", "Nante", "Walk"),
            AlphabetLetter("Ny ny", "[ɲ]", "Palatal nasal", "Nyame", "God / Supreme Sky", true),
            AlphabetLetter("O o", "[o]", "Close-mid back vowel", "Obi", "Someone"),
            AlphabetLetter("Ɔ ɔ", "[ɔ]", "Open-mid back vowel (open O)", "Ɔhene", "King / Chief", true),
            AlphabetLetter("P p", "[p]", "Stop", "Papa", "Good / Father"),
            AlphabetLetter("R r", "[r]", "Rolled R", "Bra", "Come"),
            AlphabetLetter("S s", "[s]", "Fricative", "Sika", "Gold / Money"),
            AlphabetLetter("T t", "[t]", "Stop", "Tie", "Listen"),
            AlphabetLetter("Tw tw", "[t͡ɕʷ]", "Labialized palatal", "Twi", "Asante Twi language", true),
            AlphabetLetter("U u", "[u]", "Vowel", "Uro", "Tail"),
            AlphabetLetter("W w", "[w]", "Approximant", "Wo", "You"),
            AlphabetLetter("Y y", "[j]", "Approximant", "Yɛ", "Be / Do")
        ),
        toneSystem = listOf(
            ToneDetail("High Tone", "Unmarked or acute", "Mí", "Sharp high tone", "tw_high", "Pá", "Good"),
            ToneDetail("Low Tone", "Grave `", "Dò", "Deep low tone", "tw_low", "Pà", "Strike")
        ),
        minimalToneSets = emptyList(),
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Universal Welcome to Ghana",
                nativePhrase = "Akwaaba!",
                literalTranslation = "You have entered / Welcome",
                englishMeaning = "Welcome!",
                politeResponse = "Medaase (Thank you)",
                culturalEtiquette = "The national symbol of hospitality in Ghana."
            ),
            CulturalContextGreeting(
                situation = "How are you? / Casual inquiry",
                nativePhrase = "Ɛte sɛn?",
                literalTranslation = "How is it placed / How does it stand?",
                englishMeaning = "How are you?",
                politeResponse = "Ɛyɛ / Bɔkɔɔ (It is good / Smooth & calm)",
                culturalEtiquette = "Always reply 'Bɔkɔɔ' with a calm smile."
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "tw_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "Akan Day Names (Kradin / Soul Names)",
                ruleSummary = "Every Akan child receives an automatic soul name based on the day of the week they were born, linked to spiritual attributes.",
                formulaPattern = "Day → Male Name / Female Name",
                examples = listOf(
                    GrammarExample("Monday → Kojo / Adwoa", "Kojo (Male) / Adwoa (Female)", "Peaceful soul"),
                    GrammarExample("Friday → Kofi / Afia", "Kofi (Male) / Afia (Female)", "Fertility & wandering wanderer"),
                    GrammarExample("Saturday → Kwame / Ama", "Kwame (Male) / Ama (Female)", "Ancient wisdom / God's day")
                ),
                commonMistake = "Assuming day names are nicknames; they are primary sacred identity markers."
            )
        ),
        culturalPhilosophyTitle = "Sankofa & Adinkra Wisdom",
        culturalPhilosophyText = "'Se wo were fi na wosankofa a yenkyi' (It is not wrong to go back and fetch what you forgot). Looking back into roots to build the future.",
        aiTutorLinguisticDirectives = "Enforce Akan open vowels (Ɛ, Ɔ), day-names, and Adinkra symbol philosophy."
    )

    private val amharicProfile = LinguisticProfile(
        language = AfricanLanguage.AMHARIC,
        alphabet = listOf(
            AlphabetLetter("ሀ (Ha)", "[hə]", "First order syllable", "ሀገር (Hager)", "Country"),
            AlphabetLetter("ለ (Le)", "[lə]", "First order syllable", "ልብ (Lib)", "Heart"),
            AlphabetLetter("ሐ (Hha)", "[ħə]", "Pharyngealized H", "ሕይወት (Hiwot)", "Life"),
            AlphabetLetter("መ (Me)", "[mə]", "First order syllable", "መንገድ (Menged)", "Road / Way"),
            AlphabetLetter("ሠ (Sze)", "[sə]", "Sibilant", "ሠርግ (Serg)", "Wedding"),
            AlphabetLetter("ረ (Re)", "[rə]", "Rolled R", "ሩቅ (Ruq)", "Far"),
            AlphabetLetter("ሰ (Se)", "[sə]", "Alveolar sibilant", "ሰላም (Selam)", "Peace / Hello"),
            AlphabetLetter("ቀ (Qe)", "[kʼə]", "Ejective ejective velar stop", "ቀን (Qen)", "Day", true),
            AlphabetLetter("በ (Be)", "[bə]", "Bilabial stop", "ቤት (Bēt)", "House"),
            AlphabetLetter("ተ (Te)", "[tə]", "Alveolar stop", "ትምህርት (Timhirt)", "School / Learning"),
            AlphabetLetter("ቸ (Che)", "[t͡ʃə]", "Postalveolar", "ቻይ (Chay)", "Patient / Tolerant"),
            AlphabetLetter("አ (A)", "[ʔə]", "Glottal vowel", "አመሰግናለሁ (Ameseginalehu)", "Thank you"),
            AlphabetLetter("ከ (Ke)", "[kə]", "Velar stop", "ከተማ (Ketema)", "City"),
            AlphabetLetter("ወ (We)", "[wə]", "Approximant", "ውሃ (Wuha)", "Water"),
            AlphabetLetter("ዘ (Ze)", "[zə]", "Fricative", "ዝናብ (Zinab)", "Rain"),
            AlphabetLetter("የ (Ye)", "[jə]", "Approximant", "ሰው (Sew)", "Person"),
            AlphabetLetter("ደ (De)", "[də]", "Alveolar stop", "ደህና (Dehna)", "Fine / Well"),
            AlphabetLetter("ጀ (Je)", "[d͡ʒə]", "Affricate", "ጀበና (Jebena)", "Coffee pot"),
            AlphabetLetter("ገ (Ge)", "[ɡə]", "Velar stop", "ገበያ (Gebeya)", "Market"),
            AlphabetLetter("ጠ (Tte)", "[tʼə]", "Ejective alveolar stop", "ጥላ (Ttila)", "Umbrella / Shade", true),
            AlphabetLetter("ጨ (Cche)", "[t͡ʃʼə]", "Ejective postalveolar affricate", "ጨው (Tchew)", "Salt", true),
            AlphabetLetter("ፈ (Fe)", "[fə]", "Fricative", "ፈረስ (Feres)", "Horse"),
            AlphabetLetter("ፐ (Pe)", "[pə]", "Stop (loanwords)", "ፖስታ (Posta)", "Mail")
        ),
        toneSystem = listOf(
            ToneDetail("Non-Tonal", "Ge'ez Fidel Abugida", "Seven Orders", "Each consonant has 7 vowel forms: ä, u, i, a, e, ə, o", "am_abugida", "በ ቡ ቢ ባ ቤ ብ ቦ", "Be, Bu, Bi, Ba, Bé, Bə, Bo")
        ),
        minimalToneSets = emptyList(),
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Universal Greeting",
                nativePhrase = "ሰላም (Selam)",
                literalTranslation = "Peace",
                englishMeaning = "Hello / Peace to you",
                politeResponse = "ሰላም! ደህና ነህ? (To male) / ደህና ነሽ? (To female)",
                culturalEtiquette = "Shoulder-to-shoulder touching embrace."
            ),
            CulturalContextGreeting(
                situation = "Respectful greeting to an elder or superior",
                nativePhrase = "እንደምን ዋሉ? (Endemin walu?)",
                literalTranslation = "How did you spend the day? (Polite Irswo)",
                englishMeaning = "Good afternoon / evening (Respectful)",
                politeResponse = "እግዚአብሔር ይመስገን (Egziabher yimesgen - God be praised)",
                culturalEtiquette = "Deep bow with right hand touching left elbow."
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "am_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "Subject-Object-Verb (SOV) Word Order",
                ruleSummary = "Unlike English and Yoruba (SVO), Amharic is strictly Subject + Object + Verb. The verb ALWAYS comes at the very end of the sentence!",
                formulaPattern = "[Subject] + [Object] + [Verb]",
                examples = listOf(
                    GrammarExample("እኔ እንጀራ በላሁ።", "I (እኔ) + injera (እንጀራ) + ate (በላሁ)።", "I ate injera."),
                    GrammarExample("ውሃ እፈልጋለሁ።", "Water (ውሃ) + I want (እፈልጋለሁ)።", "I want water.")
                ),
                commonMistake = "Putting the verb in the middle like English: saying 'እኔ በላሁ እንጀራ' is grammatically incorrect in Amharic."
            )
        ),
        culturalPhilosophyTitle = "Jebena Buna & Gursha (Coffee Ceremony & Feeding with Love)",
        culturalPhilosophyText = "The sacred 3-round coffee ceremony (Abol, Tona, Baraka) and 'Gursha' (feeding a guest or loved one a large bite of food with your own hand) are the highest expressions of Ethiopian friendship.",
        aiTutorLinguisticDirectives = "Enforce SOV word order, 7 Fidel orders, and polite Irswo address."
    )

    private val zuluProfile = LinguisticProfile(
        language = AfricanLanguage.ZULU,
        alphabet = listOf(
            AlphabetLetter("A a", "[a]", "Open front vowel like 'father'", "Amanzi", "Water"),
            AlphabetLetter("B b", "[ɓ]", "Voiced bilabial implosive", "Bona", "See"),
            AlphabetLetter("C c", "[ǀ]", "Dental click. Press tip of tongue against back of upper front teeth and pull away with a 'tsk' sound", "Cela", "Ask / Request", true),
            AlphabetLetter("D d", "[d]", "Voiced alveolar stop", "Dla", "Eat"),
            AlphabetLetter("E e", "[ɛ]", "Mid front vowel like 'bed'", "Ekhaya", "At home"),
            AlphabetLetter("F f", "[f]", "Labiodental fricative", "Funda", "Learn / Read"),
            AlphabetLetter("G g", "[ɡ]", "Hard velar stop", "Gijima", "Run"),
            AlphabetLetter("H h", "[h]", "Glottal fricative", "Hamba", "Go / Walk"),
            AlphabetLetter("Hl hl", "[ɬ]", "Voiceless alveolar lateral fricative. Position tongue for 'L' and blow air out the sides", "Hlala", "Sit / Stay", true),
            AlphabetLetter("I i", "[i]", "Close front vowel as in 'see'", "Inja", "Dog"),
            AlphabetLetter("J j", "[d͡ʒ]", "Affricate as in 'judge'", "Jabulani", "Be happy / Rejoice"),
            AlphabetLetter("K k", "[k]", "Velar stop", "Khuluma", "Speak"),
            AlphabetLetter("L l", "[l]", "Lateral approximant", "Lala", "Sleep"),
            AlphabetLetter("M m", "[m]", "Bilabial nasal", "Mama", "Mother"),
            AlphabetLetter("N n", "[n]", "Alveolar nasal", "Ngena", "Enter"),
            AlphabetLetter("Ny ny", "[ɲ]", "Palatal nasal like Spanish ñ", "Nyoni", "Bird", true),
            AlphabetLetter("O o", "[ɔ]", "Open-mid back vowel as in 'pot'", "Oma", "Dry / Thirsty"),
            AlphabetLetter("P p", "[pʼ]", "Ejective bilabial stop", "Phuza", "Drink"),
            AlphabetLetter("Q q", "[ǃ]", "Alveolar/palatal click. Suction tongue flat against roof of mouth and pull sharply down like popping a cork", "Qala", "Begin / Start", true),
            AlphabetLetter("R r", "[r]", "Alveolar trill (mainly in loanwords)", "Randi", "South African Rand"),
            AlphabetLetter("S s", "[s]", "Alveolar fricative", "Sawubona", "Hello / I see you"),
            AlphabetLetter("Sh sh", "[ʃ]", "Postalveolar like 'shine'", "Shaya", "Hit / Strike"),
            AlphabetLetter("T t", "[tʼ]", "Alveolar stop", "Thola", "Find / Receive"),
            AlphabetLetter("U u", "[u]", "Close back rounded vowel like 'boot'", "Umuntu", "Person / Human"),
            AlphabetLetter("V v", "[v]", "Labiodental fricative", "Vula", "Open"),
            AlphabetLetter("W w", "[w]", "Labial-velar approximant", "Wena", "You"),
            AlphabetLetter("X x", "[ǁ]", "Lateral click. Suction sides of tongue against side teeth and snap inward, like urging on a horse", "Xola", "Forgive / Be at peace", true),
            AlphabetLetter("Y y", "[j]", "Palatal approximant", "Yebo", "Yes / Truly"),
            AlphabetLetter("Z z", "[z]", "Alveolar fricative", "Zulu", "Heaven / Sky")
        ),
        toneSystem = listOf(
            ToneDetail("High Tone", "Rising pitch", "Mí", "Sharp elevated melody", "zu_high", "Ínyanga", "Moon / Month"),
            ToneDetail("Low Tone", "Falling baseline", "Dò", "Grounded baseline melody", "zu_low", "Ìnyanga", "Traditional healer / Doctor")
        ),
        minimalToneSets = listOf(
            MinimalToneSet(
                baseLetters = "i - n - y - a - n - g - a",
                explanation = "Famous Zulu tonal distinction: high tone vs low tone on the initial prefix entirely changes the meaning:",
                variations = listOf(
                    ToneVariation("Ínyanga", "High - Low - Low", "Mí - Dò - Dò", "Moon / Month", "Inyanga ekhanyayo (The shining moon)."),
                    ToneVariation("Ìnyanga", "Low - Low - Low", "Dò - Dò - Dò", "Traditional doctor / Herbalist", "Uya enyangeni (He goes to the healer).")
                )
            )
        ),
        greetingsCatalog = listOf(
            CulturalContextGreeting(
                situation = "Universal Respectful Greeting (Singular)",
                nativePhrase = "Sawubona! (Plural: Sanibonani)",
                literalTranslation = "I see you! (We acknowledge your full dignity and presence)",
                englishMeaning = "Hello / Greetings",
                politeResponse = "Yebo, sawubona! (Yes, I see you too)",
                culturalEtiquette = "To greet someone is to validate their humanity (Ubuntu). Stand tall and maintain gentle, warm eye contact."
            ),
            CulturalContextGreeting(
                situation = "Inquiring about well-being",
                nativePhrase = "Unjani? (Plural: Ninjani?)",
                literalTranslation = "How are you placed / How is your spirit?",
                englishMeaning = "How are you?",
                politeResponse = "Ngiyaphila, unjani wena? (I am well / alive, how are you?)",
                culturalEtiquette = "Always reply 'Ngiyaphila' (I am living/fine) before asking after the other person."
            ),
            CulturalContextGreeting(
                situation = "Saying goodbye to someone staying behind",
                nativePhrase = "Sala kahle (Plural: Salani kahle)",
                literalTranslation = "Remain with goodness and peace",
                englishMeaning = "Goodbye (to one staying)",
                politeResponse = "Hamba kahle (Go well / Travel with goodness)",
                culturalEtiquette = "Distinct goodbye for the one leaving vs the one staying!"
            )
        ),
        grammarRules = listOf(
            GrammarRule(
                id = "zu_g1",
                cefrLevel = ProficiencyLevel.A1,
                title = "Noun Class Prefixes (umu-/aba-, ili-/ama-, etc.)",
                ruleSummary = "isiZulu nouns are categorized into noun classes identified by their initial prefix. Class 1/2 are people: 'umu-' for singular (umuntu = person), 'aba-' for plural (abantu = people).",
                formulaPattern = "umu- [person] → aba- [people]",
                examples = listOf(
                    GrammarExample("Umuntu ngumuntu ngabantu.", "Umuntu (A person) + ngumuntu (is a person) + ngabantu (through people).", "A person is a person through other people (Ubuntu)."),
                    GrammarExample("Umfana / Abafana", "Umfana (Boy) / Abafana (Boys)", "Boy / Boys")
                ),
                commonMistake = "Omitting the initial vowel of the noun class prefix (e.g. saying 'muntu' instead of 'umuntu')."
            )
        ),
        culturalPhilosophyTitle = "Ubuntu (Umuntu ngumuntu ngabantu)",
        culturalPhilosophyText = "Ubuntu expresses the fundamental African truth that our humanity is intertwined: 'I am because we are'. In isiZulu, generosity, hospitality, communal harmony, and deep respect for life are the ultimate virtues.",
        aiTutorLinguisticDirectives = "Enforce isiZulu click consonants (c, q, x), noun prefix agreements, and Ubuntu ethics."
    )
}

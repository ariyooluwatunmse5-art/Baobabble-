package com.example.data.repository

import com.example.data.model.*

object BaobabContentRepository {

    fun getUnitsForLanguageAndLevel(language: AfricanLanguage, level: ProficiencyLevel): List<LearningUnit> {
        val allUnits = when (language) {
            AfricanLanguage.YORUBA -> getYorubaUnits()
            AfricanLanguage.SWAHILI -> getSwahiliUnits()
            AfricanLanguage.IGBO -> getIgboUnits()
            AfricanLanguage.HAUSA -> getHausaUnits()
            AfricanLanguage.TWI -> getTwiUnits()
            AfricanLanguage.AMHARIC -> getAmharicUnits()
            AfricanLanguage.ZULU -> getZuluUnits()
        }
        val matching = allUnits.filter { it.level == level }
        return if (matching.isNotEmpty()) matching else allUnits.take(2)
    }

    // -------------------------------------------------------------
    // YORUBA CURRICULUM (COMPREHENSIVE A1–C2 ARCHITECTURE)
    // -------------------------------------------------------------
    private fun getYorubaUnits(): List<LearningUnit> {
        // A1: Foundations & Greetings
        val a1Unit1 = LearningUnit(
            id = "yo_u1",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Royal Greetings & Respect (Àkíyèsí Àṣà)",
            description = "Master respectful greetings, honorific plurals (Ẹ), and essential morning courtesies.",
            iconEmoji = "👑",
            lessons = listOf(
                Lesson(
                    id = "yoruba_a1_u1_l1",
                    unitId = "yo_u1",
                    order = 1,
                    title = "Morning & Courteous Hellos",
                    topicSummary = "Master 'Ẹ n lẹ', 'Ẹ kú àárọ̀', and basic greetings",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v1",
                            instruction = "Learn this foundational respectful greeting",
                            culturalTip = "In Yoruba culture, 'Ẹ' indicates deep respect for elders, parents, or groups.",
                            nativeWord = "Ẹ kú àárọ̀",
                            phonetic = "Eh koo ah-rah-aw",
                            englishMeaning = "Good morning",
                            exampleSentence = "Ẹ kú àárọ̀, màmá mi.",
                            exampleTranslation = "Good morning, my mother.",
                            audioPhrase = "Ẹ kú àárọ̀"
                        ),
                        Exercise.MultipleChoice(
                            id = "yo_mc1",
                            instruction = "How do you respectfully greet an elder in the morning?",
                            culturalTip = "Tone mark on àárọ̀ indicates low falling register (Dò).",
                            promptText = "Good morning (respectful)",
                            options = listOf("Ẹ kú àárọ̀", "O dábọ̀", "Ẹ ṣe púpọ̀", "Báwo ni"),
                            correctIndex = 0,
                            explanation = "'Ẹ kú àárọ̀' is the polite greeting from dawn until noon.",
                            audioPhrase = "Ẹ kú àárọ̀"
                        ),
                        Exercise.MatchingPairs(
                            id = "yo_match1",
                            instruction = "Match the Yoruba phrases with their English meanings",
                            pairs = listOf(
                                PairItem("Ẹ n lẹ", "Hello / Greetings"),
                                PairItem("Ẹ kú àárọ̀", "Good morning"),
                                PairItem("Ẹ ṣe", "Thank you"),
                                PairItem("O dábọ̀", "Goodbye")
                            )
                        ),
                        Exercise.Translate(
                            id = "yo_trans1",
                            instruction = "Translate to Yoruba: 'Thank you very much'",
                            sourceText = "Thank you very much",
                            wordBank = listOf("Ẹ", "ṣe", "púpọ̀", "Báwo", "kú", "ni", "àárọ̀"),
                            correctTokens = listOf("Ẹ", "ṣe", "púpọ̀"),
                            expectedTranslation = "Ẹ ṣe púpọ̀",
                            audioPhrase = "Ẹ ṣe púpọ̀"
                        ),
                        Exercise.Speaking(
                            id = "yo_spk1",
                            instruction = "Practice pronouncing this polite greeting out loud",
                            culturalTip = "A slight bow or curtsy is customary when speaking to elders.",
                            targetPhrase = "Ẹ n lẹ o!",
                            phoneticGuide = "Eh nleh oh!",
                            englishMeaning = "Greetings / Hello to you!",
                            toneAdvice = "Mid tone rising slightly on the final 'o'."
                        )
                    )
                ),
                Lesson(
                    id = "yoruba_a1_u1_l2",
                    unitId = "yo_u1",
                    order = 2,
                    title = "How Are Things? (Báwo Ni?)",
                    topicSummary = "Ask 'Báwo ni?' and reply 'Àlàáfíà ni'",
                    xpReward = 25,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v2",
                            instruction = "Learn how to ask 'How are things?'",
                            culturalTip = "Casual greeting between peers and friends.",
                            nativeWord = "Báwo ni?",
                            phonetic = "Bah-woh nee",
                            englishMeaning = "How are you? / How is it?",
                            exampleSentence = "Ọ̀rẹ́ mi, báwo ni?",
                            exampleTranslation = "My friend, how are things?",
                            audioPhrase = "Báwo ni?"
                        ),
                        Exercise.SentenceBuilder(
                            id = "yo_sb1",
                            instruction = "Arrange words to say: 'Everything is peaceful / good'",
                            englishMeaning = "I am in peace / It is well",
                            scrambledTokens = listOf("ni", "Àlàáfíà"),
                            correctOrder = listOf("Àlàáfíà", "ni"),
                            audioPhrase = "Àlàáfíà ni"
                        ),
                        Exercise.Listening(
                            id = "yo_lis1",
                            instruction = "Listen and select what the speaker said",
                            spokenPhrase = "Ẹ ṣe púpọ̀",
                            phoneticHint = "Eh shey poo-poh",
                            options = listOf("Thank you very much", "Good afternoon", "I am fine", "Where are you?"),
                            correctIndex = 0
                        )
                    )
                )
            )
        )

        val a1Unit2 = LearningUnit(
            id = "yo_u2",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.A1,
            unitNumber = 2,
            title = "Numbers, Wealth & Counting (Ọ̀kọ̀ọ̀kan sí Ogún)",
            description = "Learn Yoruba numerals from 1 to 20, currency terms, and basic counting rhythms.",
            iconEmoji = "🔢",
            lessons = listOf(
                Lesson(
                    id = "yoruba_a1_u2_l1",
                    unitId = "yo_u2",
                    order = 1,
                    title = "Counting 1 to 5 (Ọ̀kan sí Àrún)",
                    topicSummary = "Ení, èjì, ẹ̀ta, ẹ̀rin, àrún",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v_num1",
                            instruction = "Learn the word for One (1)",
                            culturalTip = "Yoruba traditionally counted with cowrie shells (Owó ẹyọ).",
                            nativeWord = "Ọ̀kan (Ení)",
                            phonetic = "Aw-kan",
                            englishMeaning = "One (1)",
                            exampleSentence = "Isu ọ̀kan péré.",
                            exampleTranslation = "Only one yam.",
                            audioPhrase = "Ọ̀kan"
                        ),
                        Exercise.MatchingPairs(
                            id = "yo_match_num",
                            instruction = "Match numbers to digits",
                            pairs = listOf(
                                PairItem("Ọ̀kan", "1"),
                                PairItem("Èjì", "2"),
                                PairItem("Ẹ̀ta", "3"),
                                PairItem("Ẹ̀rin", "4")
                            )
                        ),
                        Exercise.MultipleChoice(
                            id = "yo_mc_num",
                            instruction = "What does 'Àrún' represent?",
                            promptText = "Àrún",
                            options = listOf("Five (5)", "Ten (10)", "Three (3)", "Zero"),
                            correctIndex = 0,
                            explanation = "'Àrún' is five (5).",
                            audioPhrase = "Àrún"
                        )
                    )
                )
            )
        )

        // A2: Elementary (Market, Food & Daily Life)
        val a2Unit3 = LearningUnit(
            id = "yo_u3",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.A2,
            unitNumber = 1,
            title = "Bargaining at Balogun Market (Ọjà)",
            description = "Haggle with dignity: ask 'Élò ni?', plea 'Dín owó kù', and buy food staples.",
            iconEmoji = "🧺",
            lessons = listOf(
                Lesson(
                    id = "yoruba_a2_u3_l1",
                    unitId = "yo_u3",
                    order = 1,
                    title = "How Much Is It? (Élò Ni?)",
                    topicSummary = "Negotiate prices and purchase tubers of yam",
                    xpReward = 25,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v3",
                            instruction = "Key shopping phrase",
                            culturalTip = "Always greet the market vendor before asking for the price!",
                            nativeWord = "Élò ni?",
                            phonetic = "Eh-loh nee",
                            englishMeaning = "How much is it?",
                            exampleSentence = "Isu yìí, élò ni?",
                            exampleTranslation = "This yam, how much is it?",
                            audioPhrase = "Élò ni?"
                        ),
                        Exercise.MultipleChoice(
                            id = "yo_mc2",
                            instruction = "What does 'Dín owó kù' mean in the market?",
                            promptText = "Dín owó kù",
                            options = listOf("Reduce the price please", "I don't want it", "Give me ten yams", "Where is the bank?"),
                            correctIndex = 0,
                            explanation = "'Dín owó kù' is the polite plea to haggle down the price.",
                            audioPhrase = "Dín owó kù"
                        ),
                        Exercise.Translate(
                            id = "yo_trans2",
                            instruction = "Translate: 'I want to buy yam'",
                            sourceText = "I want to buy yam",
                            wordBank = listOf("Mo", "fẹ́", "ra", "iṣu", "élò", "kù", "owó"),
                            correctTokens = listOf("Mo", "fẹ́", "ra", "iṣu"),
                            expectedTranslation = "Mo fẹ́ ra iṣu",
                            audioPhrase = "Mo fẹ́ ra iṣu"
                        )
                    )
                )
            )
        )

        val a2Unit4 = LearningUnit(
            id = "yo_u4",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.A2,
            unitNumber = 2,
            title = "Yoruba Delicacies & Feasting (Oúnjẹ)",
            description = "Talk about traditional dishes: Ẹ̀bà, Àmàlà, Ọbẹ̀ Ẹ̀fọ́, and hospitality phrases.",
            iconEmoji = "🍲",
            lessons = listOf(
                Lesson(
                    id = "yoruba_a2_u4_l1",
                    unitId = "yo_u4",
                    order = 1,
                    title = "Àmàlà, Ẹ̀bà & Soups",
                    topicSummary = "Ordering and describing delicious meals",
                    xpReward = 25,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v_amala",
                            instruction = "Iconic Yoruba meal made from yam flour",
                            culturalTip = "Àmàlà is best eaten hot with gbẹ̀gìrì (bean soup) and ewédú.",
                            nativeWord = "Àmàlà àti Ewédú",
                            phonetic = "Ah-mah-lah ah-tee Eh-way-doo",
                            englishMeaning = "Amala with Ewedu soup",
                            exampleSentence = "Mo fẹ́ jẹ àmàlà gbígbóná.",
                            exampleTranslation = "I want to eat piping hot amala.",
                            audioPhrase = "Àmàlà àti Ewédú"
                        ),
                        Exercise.MatchingPairs(
                            id = "yo_match_food",
                            instruction = "Match food words",
                            pairs = listOf(
                                PairItem("Omi", "Water"),
                                PairItem("Oúnjẹ", "Food"),
                                PairItem("Ẹja", "Fish"),
                                PairItem("Ẹran", "Meat")
                            )
                        )
                    )
                )
            )
        )

        // B1: Intermediate (Travel & Moving Around)
        val b1Unit5 = LearningUnit(
            id = "yo_u5",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.B1,
            unitNumber = 1,
            title = "Danfo Transit & Directions (Ìrìn-Àjò)",
            description = "Navigate yellow Danfo buses in Lagos, ask directions, and express movement.",
            iconEmoji = "🚌",
            lessons = listOf(
                Lesson(
                    id = "yoruba_b1_u5_l1",
                    unitId = "yo_u5",
                    order = 1,
                    title = "Where Are You Going? (Níbo lo ń lọ?)",
                    topicSummary = "Conductor calls and asking route details",
                    xpReward = 30,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v_transit",
                            instruction = "Essential travel question",
                            culturalTip = "Lagos Danfo conductors call out bus stops in quick, rhythmic cadence.",
                            nativeWord = "Níbo lo ń lọ?",
                            phonetic = "Nee-baw loh n law?",
                            englishMeaning = "Where are you going?",
                            exampleSentence = "Èkó ni mo ń lọ.",
                            exampleTranslation = "I am going to Lagos.",
                            audioPhrase = "Níbo lo ń lọ?"
                        ),
                        Exercise.SentenceBuilder(
                            id = "yo_sb_bus",
                            instruction = "Build: 'I am going to the island'",
                            englishMeaning = "I am going to the island",
                            scrambledTokens = listOf("lọ", "sí", "erékùṣù", "Mo", "ń"),
                            correctOrder = listOf("Mo", "ń", "lọ", "sí", "erékùṣù"),
                            audioPhrase = "Mo ń lọ sí erékùṣù"
                        )
                    )
                )
            )
        )

        // B2: Upper-Intermediate (Kinship & Ceremonies)
        val b2Unit6 = LearningUnit(
            id = "yo_u6",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.B2,
            unitNumber = 1,
            title = "Kinship Lineage & Festivals (Ẹbí àti Àjọyọ̀)",
            description = "Family lineage, Ìsọmọlórúkọ (naming ceremonies), and wedding protocols.",
            iconEmoji = "🎉",
            lessons = listOf(
                Lesson(
                    id = "yoruba_b2_u6_l1",
                    unitId = "yo_u6",
                    order = 1,
                    title = "Naming the Child (Ìsọmọlórúkọ)",
                    topicSummary = "Honey, water, and salt symbols during naming",
                    xpReward = 35,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v_naming",
                            instruction = "Symbol of sweetness in naming ceremonies",
                            culturalTip = "Oyin (honey) is placed on the baby's tongue wishing their life to be sweet.",
                            nativeWord = "Oyin",
                            phonetic = "Aw-yeen",
                            englishMeaning = "Honey (sweetness of life)",
                            exampleSentence = "Ayé ọmọ yìí yóò dùn bí oyin.",
                            exampleTranslation = "The life of this child will be sweet as honey.",
                            audioPhrase = "Oyin"
                        )
                    )
                )
            )
        )

        // C1: Advanced (Proverbs & Wisdom Tales)
        val c1Unit7 = LearningUnit(
            id = "yo_u7",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.C1,
            unitNumber = 1,
            title = "Òwe Yorùbá (The Steed of Speech)",
            description = "\"Òwe l'ẹṣin ọ̀rọ̀\" — Proverbs are the horses of speech used to unlock deep truths.",
            iconEmoji = "📜",
            lessons = listOf(
                Lesson(
                    id = "yoruba_c1_u7_l1",
                    unitId = "yo_u7",
                    order = 1,
                    title = "Proverbial Wit & Moral Foundations",
                    topicSummary = "Master metaphorical proverbs and situational oratory",
                    xpReward = 40,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v_owe",
                            instruction = "The definition of traditional proverbs",
                            culturalTip = "Yoruba elders say when speech is lost, a proverb is sent to fetch it.",
                            nativeWord = "Òwe l'ẹṣin ọ̀rọ̀",
                            phonetic = "Oh-way leh-sheen aw-raw",
                            englishMeaning = "Proverbs are the horse of speech",
                            exampleSentence = "Bí ọ̀rọ̀ bá sọnù, òwe la fi ń wá a.",
                            exampleTranslation = "If truth is lost, we use a proverb to retrieve it.",
                            audioPhrase = "Òwe l'ẹṣin ọ̀rọ̀"
                        ),
                        Exercise.MultipleChoice(
                            id = "yo_mc_owe1",
                            instruction = "Complete the proverb: 'Ilé la ti ń kọ́ ẹ̀ṣọ́...'",
                            promptText = "Ilé la ti ń kọ́ ẹ̀ṣọ́...",
                            options = listOf("ròde", "sùn", "jẹun", "ra"),
                            correctIndex = 0,
                            explanation = "'Ilé la ti ń kọ́ ẹ̀ṣọ́ ròde' means noble character and decorum start at home.",
                            audioPhrase = "Ilé la ti ń kọ́ ẹ̀ṣọ́ ròde"
                        )
                    )
                )
            )
        )

        // C2: Mastery (Ọmọlúwàbí Oratory & Royal Court)
        val c2Unit8 = LearningUnit(
            id = "yo_u8",
            languageId = AfricanLanguage.YORUBA.id,
            level = ProficiencyLevel.C2,
            unitNumber = 1,
            title = "Ọmọlúwàbí Oratory & Royal Court (Àṣẹ Ọba)",
            description = "Courtly address to the Ọba (King), ancient praise chants (Oríkì), and philosophical oratory.",
            iconEmoji = "🌳",
            lessons = listOf(
                Lesson(
                    id = "yoruba_c2_u8_l1",
                    unitId = "yo_u8",
                    order = 1,
                    title = "Kábíyèsí & Court Protocol",
                    topicSummary = "Royal greetings: 'Kábíyèsí, Káááábíyèsí o!' and ancestral praise",
                    xpReward = 50,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "yo_v_kabiyesi",
                            instruction = "Highest royal salutation to a crowned Yoruba King",
                            culturalTip = "Literally means 'The one whose questions no one may question'.",
                            nativeWord = "Kábíyèsí o!",
                            phonetic = "Kah-bee-yeh-see oh!",
                            englishMeaning = "Your Royal Majesty / Long live the King!",
                            exampleSentence = "Kábíyèsí o! Kí adé pẹ́ lórí, kí bàtà pẹ́ lẹ́sẹ̀.",
                            exampleTranslation = "Your Majesty! May the crown endure on your head and shoes on your feet.",
                            audioPhrase = "Kábíyèsí o"
                        )
                    )
                )
            )
        )

        return listOf(a1Unit1, a1Unit2, a2Unit3, a2Unit4, b1Unit5, b2Unit6, c1Unit7, c2Unit8)
    }

    // -------------------------------------------------------------
    // SWAHILI CURRICULUM
    // -------------------------------------------------------------
    private fun getSwahiliUnits(): List<LearningUnit> {
        val unit1 = LearningUnit(
            id = "swahili_u1",
            languageId = AfricanLanguage.SWAHILI.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Karibu! Warm East African Greetings",
            description = "Learn classic Swahili greetings, polite responses, and the joyful culture of Karibu.",
            iconEmoji = "☀️",
            lessons = listOf(
                Lesson(
                    id = "swahili_a1_u1_l1",
                    unitId = "swahili_u1",
                    order = 1,
                    title = "Jambo & Habari Gani?",
                    topicSummary = "Essential greetings across Kenya & Tanzania",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "sw_v1",
                            instruction = "The universal welcoming word",
                            culturalTip = "'Karibu' means Welcome, while 'Asante' means Thank you.",
                            nativeWord = "Karibu",
                            phonetic = "Kah-ree-boo",
                            englishMeaning = "Welcome",
                            exampleSentence = "Karibu nyumbani kwangu.",
                            exampleTranslation = "Welcome to my home.",
                            audioPhrase = "Karibu"
                        ),
                        Exercise.MultipleChoice(
                            id = "sw_mc1",
                            instruction = "How do you reply to 'Habari gani?' (How are things?)",
                            promptText = "Habari gani?",
                            options = listOf("Nzuri sana (Very good)", "Kwaheri (Goodbye)", "Hapana (No)", "Pole (Sorry)"),
                            correctIndex = 0,
                            explanation = "'Nzuri sana' means 'Very good' or 'Fine'.",
                            audioPhrase = "Nzuri sana"
                        ),
                        Exercise.MatchingPairs(
                            id = "sw_match1",
                            instruction = "Match Swahili words with their English equivalents",
                            pairs = listOf(
                                PairItem("Asante sana", "Thank you very much"),
                                PairItem("Karibu", "Welcome"),
                                PairItem("Habari yako?", "How are you?"),
                                PairItem("Kwaheri", "Goodbye")
                            )
                        ),
                        Exercise.SentenceBuilder(
                            id = "sw_sb1",
                            instruction = "Build: 'Thank you very much my friend'",
                            englishMeaning = "Thank you very much my friend",
                            scrambledTokens = listOf("rafiki", "sana", "Asante", "yangu"),
                            correctOrder = listOf("Asante", "sana", "rafiki", "yangu"),
                            audioPhrase = "Asante sana rafiki yangu"
                        ),
                        Exercise.Speaking(
                            id = "sw_spk1",
                            instruction = "Pronounce this respectful phrase clearly",
                            culturalTip = "Stress is almost always on the second to last syllable in Swahili.",
                            targetPhrase = "Habari ya asubuhi!",
                            phoneticGuide = "Hah-BAH-ree yah ah-soo-BOO-hee!",
                            englishMeaning = "Good morning!",
                            toneAdvice = "Smooth stress on the second-to-last syllable: ah-su-BU-hi."
                        )
                    )
                )
            )
        )
        return listOf(unit1)
    }

    // -------------------------------------------------------------
    // IGBO CURRICULUM
    // -------------------------------------------------------------
    private fun getIgboUnits(): List<LearningUnit> {
        val unit1 = LearningUnit(
            id = "igbo_u1",
            languageId = AfricanLanguage.IGBO.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Igbo Foundations (Nnọọ & Ụtụtụ Ọma)",
            description = "Explore foundational greetings, tone awareness, and Igbo hospitable warmth.",
            iconEmoji = "🦅",
            lessons = listOf(
                Lesson(
                    id = "igbo_a1_u1_l1",
                    unitId = "igbo_u1",
                    order = 1,
                    title = "Morning & Warm Welcomes",
                    topicSummary = "Master 'Kèdú', 'Ụtụtụ ọma', and 'Dàálụ'",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "ig_v1",
                            instruction = "The ubiquitous Igbo greeting",
                            culturalTip = "'Kèdú' literally means 'How?' and is used with elders and youth.",
                            nativeWord = "Kèdú?",
                            phonetic = "Kay-doo",
                            englishMeaning = "How are you? / Hello",
                            exampleSentence = "Kèdú kwanụ, nwanne m?",
                            exampleTranslation = "How are you doing, my sibling?",
                            audioPhrase = "Kèdú kwanụ?"
                        ),
                        Exercise.MultipleChoice(
                            id = "ig_mc1",
                            instruction = "What does 'Dàálụ' mean in Igbo?",
                            promptText = "Dàálụ",
                            options = listOf("Thank you / Well done", "Good night", "I am going", "Water"),
                            correctIndex = 0,
                            explanation = "'Dàálụ' is the cherished expression of gratitude and appreciation.",
                            audioPhrase = "Dàálụ"
                        )
                    )
                )
            )
        )
        return listOf(unit1)
    }

    // -------------------------------------------------------------
    // HAUSA CURRICULUM
    // -------------------------------------------------------------
    private fun getHausaUnits(): List<LearningUnit> {
        val unit1 = LearningUnit(
            id = "hausa_u1",
            languageId = AfricanLanguage.HAUSA.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Peace & Politeness (Sannu da Lafiya)",
            description = "Learn standard Hausa greetings and the peaceful exchange of 'Lafiya lau'.",
            iconEmoji = "🌾",
            lessons = listOf(
                Lesson(
                    id = "hausa_a1_u1_l1",
                    unitId = "hausa_u1",
                    order = 1,
                    title = "Greetings of the Day",
                    topicSummary = "Master 'Sannu', 'Ina kwana', and 'Na gode'",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "ha_v1",
                            instruction = "Everyday respectful greeting",
                            culturalTip = "'Sannu' is multi-purpose: hello, excuse me, and sympathy.",
                            nativeWord = "Sannu",
                            phonetic = "Sahn-noo",
                            englishMeaning = "Hello / Greetings",
                            exampleSentence = "Sannu da zuwa!",
                            exampleTranslation = "Welcome!",
                            audioPhrase = "Sannu da zuwa"
                        )
                    )
                )
            )
        )
        return listOf(unit1)
    }

    // -------------------------------------------------------------
    // TWI CURRICULUM
    // -------------------------------------------------------------
    private fun getTwiUnits(): List<LearningUnit> {
        val unit1 = LearningUnit(
            id = "twi_u1",
            languageId = AfricanLanguage.TWI.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Akwaaba! Gateway to Asante Twi",
            description = "Enter Ghana with 'Akwaaba', 'Ɛte sɛn?', and the warm rhythm of Akan speech.",
            iconEmoji = "🥁",
            lessons = listOf(
                Lesson(
                    id = "twi_a1_u1_l1",
                    unitId = "twi_u1",
                    order = 1,
                    title = "Akwaaba & Daily Well-being",
                    topicSummary = "Learn 'Akwaaba', 'Me daase', and 'Bɔkɔɔ'",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "tw_v1",
                            instruction = "Ghana's iconic embrace",
                            culturalTip = "'Akwaaba' graces every entrance and airport in Ghana.",
                            nativeWord = "Akwaaba",
                            phonetic = "Ah-kwaa-bah",
                            englishMeaning = "Welcome",
                            exampleSentence = "Akwaaba ba fie!",
                            exampleTranslation = "Welcome back home!",
                            audioPhrase = "Akwaaba"
                        )
                    )
                )
            )
        )
        return listOf(unit1)
    }

    // -------------------------------------------------------------
    // AMHARIC CURRICULUM
    // -------------------------------------------------------------
    private fun getAmharicUnits(): List<LearningUnit> {
        val unit1 = LearningUnit(
            id = "amharic_u1",
            languageId = AfricanLanguage.AMHARIC.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Selam & The Ge'ez Fidel Heritage",
            description = "Discover the ancient script of Ethiopia, respectful greetings, and royal hospitality.",
            iconEmoji = "📜",
            lessons = listOf(
                Lesson(
                    id = "amharic_a1_u1_l1",
                    unitId = "amharic_u1",
                    order = 1,
                    title = "Selam (Peace) & Gratitude",
                    topicSummary = "Say 'ሰላም (Selam)' and 'አመሰግናለሁ (Ameseginalehu)'",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "am_v1",
                            instruction = "Universal Ethiopian greeting",
                            culturalTip = "Often accompanied with a double or triple shoulder-touch embrace.",
                            nativeWord = "ሰላም (Selam)",
                            phonetic = "Seh-lahm",
                            englishMeaning = "Peace / Hello",
                            exampleSentence = "ሰላም ለሁላችሁ። (Selam lehulachihu)",
                            exampleTranslation = "Peace be to you all.",
                            audioPhrase = "Selam"
                        )
                    )
                )
            )
        )
        return listOf(unit1)
    }

    // -------------------------------------------------------------
    // ISIZULU CURRICULUM (A1 FOUNDATIONS & UBUNTU)
    // -------------------------------------------------------------
    private fun getZuluUnits(): List<LearningUnit> {
        val unit1 = LearningUnit(
            id = "zu_u1",
            languageId = AfricanLanguage.ZULU.id,
            level = ProficiencyLevel.A1,
            unitNumber = 1,
            title = "Sawubona & The Spirit of Ubuntu (IsiZulu)",
            description = "Master authentic isiZulu greetings, respect markers, and click consonant phonetics.",
            iconEmoji = "🇿🇦",
            lessons = listOf(
                Lesson(
                    id = "zulu_a1_u1_l1",
                    unitId = "zu_u1",
                    order = 1,
                    title = "Sawubona: Seeing the Soul",
                    topicSummary = "Master 'Sawubona', 'Yebo', and Ubuntu greetings",
                    xpReward = 20,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "zu_v1",
                            instruction = "Foundational greeting of human recognition",
                            culturalTip = "Sawubona translates to 'I see you'—acknowledging the other person's full humanity and ancestors (Ubuntu).",
                            nativeWord = "Sawubona",
                            phonetic = "Sah-woo-boh-nah",
                            englishMeaning = "Hello (I see you)",
                            exampleSentence = "Sawubona mngane wami.",
                            exampleTranslation = "Hello, my friend.",
                            audioPhrase = "Sawubona"
                        ),
                        Exercise.VocabularyIntro(
                            id = "zu_v2",
                            instruction = "Polite response to Sawubona",
                            culturalTip = "'Yebo' signifies agreement and respect when greeting or conversing.",
                            nativeWord = "Yebo, sawubona",
                            phonetic = "Yeh-boh, sah-woo-boh-nah",
                            englishMeaning = "Yes, hello (I see you too)",
                            exampleSentence = "Yebo, sawubona gogo.",
                            exampleTranslation = "Yes, hello grandmother.",
                            audioPhrase = "Yebo, sawubona"
                        ),
                        Exercise.MultipleChoice(
                            id = "zu_mc1",
                            instruction = "How do you respond when an elder says 'Sawubona'?",
                            culturalTip = "Respectfully affirm with 'Yebo' and return the greeting.",
                            promptText = "Response to 'Sawubona'",
                            options = listOf("Yebo, sawubona", "Hamba kahle", "Uxolo", "Cha"),
                            correctIndex = 0,
                            explanation = "'Yebo, sawubona' affirms and returns the greeting with reciprocal respect."
                        ),
                        Exercise.MatchingPairs(
                            id = "zu_m1",
                            instruction = "Match the foundational isiZulu greetings",
                            culturalTip = "'Hamba kahle' is said to the person leaving; 'Sala kahle' to the person staying.",
                            pairs = listOf(
                                PairItem("Sawubona", "Hello / I see you"),
                                PairItem("Ngiyabonga", "Thank you"),
                                PairItem("Hamba kahle", "Go well (Goodbye)"),
                                PairItem("Sala kahle", "Stay well (Goodbye)")
                            )
                        ),
                        Exercise.MultipleChoice(
                            id = "zu_fib1",
                            instruction = "Complete the farewell phrase to someone leaving",
                            culturalTip = "Use 'Hamba' (go) for departing people, 'Sala' (stay) for hosts.",
                            promptText = "___ kahle! (Go well)",
                            options = listOf("Hamba", "Sala", "Yebo", "Uxolo"),
                            correctIndex = 0,
                            explanation = "'Hamba' means go. 'Hamba kahle' means go well."
                        )
                    )
                ),
                Lesson(
                    id = "zulu_a1_u1_l2",
                    unitId = "zu_u1",
                    order = 2,
                    title = "Click Consonants & Gratitude",
                    topicSummary = "Dental 'c', alveolar 'q', and lateral 'x' clicks",
                    xpReward = 25,
                    exercises = listOf(
                        Exercise.VocabularyIntro(
                            id = "zu_v3",
                            instruction = "Dental click 'c' practice",
                            culturalTip = "Pronounce the letter 'c' by touching the tongue tip behind upper front teeth and pulling back sharply, like 'tsk-tsk'.",
                            nativeWord = "Ngiyacela",
                            phonetic = "Ngee-yah-[ǀ]e-lah",
                            englishMeaning = "Please / I request politely",
                            exampleSentence = "Ngiyacela usizo.",
                            exampleTranslation = "Please, I need help.",
                            audioPhrase = "Ngiyacela"
                        ),
                        Exercise.MultipleChoice(
                            id = "zu_mc2",
                            instruction = "Which mouth position produces the dental click 'c' in isiZulu?",
                            culturalTip = "The dental click [ǀ] is identical to the English 'tsk-tsk' sound of disapproval, but carries neutral linguistic tone in Bantu languages.",
                            promptText = "Phonetic articulation of the 'c' click",
                            options = listOf("Tongue behind upper front teeth (tsk-tsk)", "Tongue pressed hard against palate (cork pop)", "Sides of tongue against molars (horse click)", "Lips popping together"),
                            correctIndex = 0,
                            explanation = "'c' is a dental click produced right behind the upper incisors."
                        ),
                        Exercise.SentenceBuilder(
                            id = "zu_sb1",
                            instruction = "Assemble: 'Thank you very much'",
                            culturalTip = "'Kakhulu' intensifies any verb or expression of gratitude.",
                            englishMeaning = "Thank you very much",
                            scrambledTokens = listOf("kakhulu", "Ngiyabonga", "mngane"),
                            correctOrder = listOf("Ngiyabonga", "kakhulu"),
                            audioPhrase = "Ngiyabonga kakhulu"
                        )
                    )
                )
            )
        )
        return listOf(unit1)
    }

    // -------------------------------------------------------------
    // CONVERSATION SCENARIOS
    // -------------------------------------------------------------
    fun getConversationScenarios(language: AfricanLanguage): List<ConversationScenario> {
        return when (language) {
            AfricanLanguage.YORUBA -> listOf(
                ConversationScenario(
                    id = "yo_scen_market",
                    language = AfricanLanguage.YORUBA,
                    title = "Balogun Market Bargaining",
                    situation = "You are at Balogun Market in Lagos island buying yams and colorful Ankara fabric.",
                    location = "Balogun Market, Lagos",
                    roleName = "Iya Oloja (Market Vendor)",
                    roleAvatar = "👵🏾",
                    difficulty = "Beginner (A1/A2)",
                    initialMessage = "Ẹ n lẹ o, ọmọ mi! Kí lo fẹ́ ra lónìí? A ní isu tútù àti ankara dídára.",
                    initialTranslation = "Greetings, my child! What would you like to buy today? We have fresh yams and quality Ankara fabric.",
                    suggestedReplies = listOf(
                        "Ẹ kú àárọ̀ màmá! Isu yìí élò ni?",
                        "Mo fẹ́ ra ankara, dín owó kù fún mi.",
                        "Báwo ni ọjà ṣe ń lọ?"
                    )
                ),
                ConversationScenario(
                    id = "yo_scen_family",
                    language = AfricanLanguage.YORUBA,
                    title = "Visiting the Elders in Ibadan",
                    situation = "You arrive at your grandparent's family compound for a celebration.",
                    location = "Family Compound, Ibadan",
                    roleName = "Bàbá Àgbà (Grandfather)",
                    roleAvatar = "👴🏾",
                    difficulty = "Intermediate (B1)",
                    initialMessage = "Ẹ kú àbọ̀, ọmọ mi! Ṣé àlàáfíà ni o wà? Báwo ni èkó ṣe rí?",
                    initialTranslation = "Welcome back, my child! Are you well? How is Lagos?",
                    suggestedReplies = listOf(
                        "Àlàáfíà ni mo wà, bàbá mi! Ẹ ṣe púpọ̀.",
                        "Mo dúpẹ́ lọ́wọ́ Ọlọ́run, gbogbo nǹkan ń lọ dáadáa.",
                        "Mo mú ẹ̀bùn wá fún yín."
                    )
                )
            )
            AfricanLanguage.SWAHILI -> listOf(
                ConversationScenario(
                    id = "sw_scen_matatu",
                    language = AfricanLanguage.SWAHILI,
                    title = "Riding a Nairobi Matatu",
                    situation = "You are taking a vibrant matatu bus from Westlands to Nairobi CBD.",
                    location = "Nairobi CBD",
                    roleName = "Makanga (Bus Conductor)",
                    roleAvatar = "🚐",
                    difficulty = "Beginner (A1)",
                    initialMessage = "Nairobi town! Hamsa hamsa tu, ingia twende sasa hivi!",
                    initialTranslation = "Nairobi city center! Fifty shillings only, step in let us go right now!",
                    suggestedReplies = listOf(
                        "Habari yako! Je, hii inafika katikati ya mji?",
                        "Nitaacha hapa kwenye kituo kinachofuata.",
                        "Chukua nauli yangu, asante sana."
                    )
                )
            )
            AfricanLanguage.IGBO -> listOf(
                ConversationScenario(
                    id = "ig_scen_compound",
                    language = AfricanLanguage.IGBO,
                    title = "Village Gathering in Enugu",
                    situation = "You join the community meeting under the village mango tree.",
                    location = "Enugu Community Hall",
                    roleName = "Ichie (Village Elder)",
                    roleAvatar = "🧓🏾",
                    difficulty = "Beginner (A1)",
                    initialMessage = "Nnọọ nwanne m! Kèdú kwanụ maka ndị ezinụlọ gị?",
                    initialTranslation = "Welcome my sibling! How is your family doing?",
                    suggestedReplies = listOf(
                        "Ha dị mma nke ukwuu, dàálụ!",
                        "Ụtụtụ ọma Ichie, ọ dị m mma ịhụ gị.",
                        "Ka udo dị n'ime obodo anyị."
                    )
                )
            )
            AfricanLanguage.HAUSA -> listOf(
                ConversationScenario(
                    id = "ha_scen_kwanan",
                    language = AfricanLanguage.HAUSA,
                    title = "Kurmi Market Spice Stalls",
                    situation = "Browsing ancient spices, leather slippers, and fragrant herbs in Kano.",
                    location = "Kurmi Market, Kano",
                    roleName = "Malam Danladi (Trader)",
                    roleAvatar = "👳🏾",
                    difficulty = "Beginner (A1)",
                    initialMessage = "Sannu da zuwa babban birnin Kano! Me kake nema a kasuwarmu?",
                    initialTranslation = "Welcome to the great city of Kano! What are you searching for in our market?",
                    suggestedReplies = listOf(
                        "Sannu Malam, ina neman kayan yaji da zobo.",
                        "Nawa ne kudin wadannan takalma?",
                        "Na gode sosai da karamcin ku."
                    )
                )
            )
            AfricanLanguage.TWI -> listOf(
                ConversationScenario(
                    id = "tw_scen_kumasi",
                    language = AfricanLanguage.TWI,
                    title = "Kejetia Market Kente Weaving",
                    situation = "You visit the artisan weavers of Kumasi to choose hand-woven royal Kente cloth.",
                    location = "Kumasi Cultural Centre",
                    roleName = "Agya Kofi (Master Weaver)",
                    roleAvatar = "👑",
                    difficulty = "Beginner (A1)",
                    initialMessage = "Akwaaba wo Kumasi! Ɔkyena kente foforɔ yi yɛ fɛ pa ara.",
                    initialTranslation = "Welcome to Kumasi! This newly woven Kente pattern is very beautiful.",
                    suggestedReplies = listOf(
                        "Me daase Agya Kofi, me pɛ kente kɔkɔɔ yi.",
                        "Ɛyɛ fɛ pa ara! Sika ahe na wogye?",
                        "Ɛte sɛn adwuma no?"
                    )
                )
            )
            AfricanLanguage.AMHARIC -> listOf(
                ConversationScenario(
                    id = "am_scen_buna",
                    language = AfricanLanguage.AMHARIC,
                    title = "Addis Ababa Traditional Coffee Ceremony",
                    situation = "You partake in the sacred 3-round Jebena Buna coffee ceremony.",
                    location = "Piazza, Addis Ababa",
                    roleName = "Emaye Tigist (Host)",
                    roleAvatar = "☕",
                    difficulty = "Beginner (A1)",
                    initialMessage = "እንኳን ደህና መጣህ! ጀበና ቡናው እየፈላ ነው። (Inkuan dehna metah! Jebena bunaw eyefela new.)",
                    initialTranslation = "Welcome! The traditional clay pot coffee is brewing right now.",
                    suggestedReplies = listOf(
                        "ሰላም! አመሰግናለሁ እማዬ፣ ቡና እወዳለሁ።",
                        "እንዴት ነሽ? ቡናው በጣም ጥሩ መዓዛ አለው።",
                        "የኢትዮጵያ ባህል በጣም ያምራል።"
                    )
                )
            )
            AfricanLanguage.ZULU -> listOf(
                ConversationScenario(
                    id = "zu_scen_market",
                    language = AfricanLanguage.ZULU,
                    title = "Victoria Street Market in Durban",
                    situation = "You visit Durban's vibrant Victoria Street spice and beadwork market.",
                    location = "Victoria Street Market, Durban",
                    roleName = "Gogo Nomusa (Beadwork Artisan)",
                    roleAvatar = "🧺",
                    difficulty = "Beginner (A1)",
                    initialMessage = "Sawubona mntanami! Wamukelekile emakethe. Ufuna ukubona ubuhlalu baseZulu?",
                    initialTranslation = "Hello my child! Welcome to the market. Would you like to see authentic Zulu beadwork?",
                    suggestedReplies = listOf(
                        "Yebo gogo, ngiyabonga! Ngicela ukubona.",
                        "Kunjani gogo? Ubuhlalu buhle kakhulu.",
                        "Kuyimalini lokhu?"
                    )
                )
            )
        }
    }

    fun getAllAchievements(): List<Achievement> {
        return listOf(
            Achievement("first_seed", "First Seed", "Complete your very first African language lesson", "🌱", 50, true),
            Achievement("streak_3", "Firekeeper", "Maintain an unbroken 3-day learning streak", "🔥", 75, true),
            Achievement("market_master", "Savanna Trader", "Complete any market bargaining exercise with 100% accuracy", "🧺", 100, false),
            Achievement("polyglot_roots", "Great Baobab", "Study lessons across 3 or more African languages", "🌳", 150, false),
            Achievement("tone_perfection", "Melodic Tongue", "Master tonal inflections without losing a single heart", "🎵", 120, false),
            Achievement("elder_wisdom", "Proverbial Elder", "Unlock and read 5 traditional African proverbs", "📜", 200, false)
        )
    }

    fun getDailyQuests(): List<DailyQuest> {
        return listOf(
            DailyQuest("quest_xp", "Daily Sun Harvest", "Earn 50 XP today", 50, 30, 25),
            DailyQuest("quest_lessons", "Path Walker", "Complete 2 lessons or review sessions", 2, 1, 30),
            DailyQuest("quest_pronounce", "Voice of the Savanna", "Practice pronunciation in 1 speaking challenge", 1, 0, 20)
        )
    }
}

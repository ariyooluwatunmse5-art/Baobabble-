import subprocess
import os

font_bold = "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"
font_regular = "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"

# 1. Generate icon-512.png
cmd_icon = [
    "convert", "-size", "512x512", "radial-gradient:#1e293b-#0a0f0d",
    # Rounded badge background
    "-fill", "#16a34a", "-draw", "roundrectangle 32,32 480,480 96,96",
    "-fill", "#22c55e", "-draw", "roundrectangle 44,44 468,468 84,84",
    "-fill", "#052e16", "-draw", "roundrectangle 56,56 456,456 72,72",
    # Tree trunk / Baobab silhouette
    "-fill", "#22c55e",
    "-draw", "polygon 220,410 292,410 276,280 236,280",
    # Roots
    "-draw", "line 220,410 180,440",
    "-draw", "line 292,410 332,440",
    # Foliage branches
    "-draw", "circle 256,220 256,150",
    "-draw", "circle 190,240 190,190",
    "-draw", "circle 322,240 322,190",
    "-draw", "circle 220,180 220,135",
    "-draw", "circle 292,180 292,135",
    # Accent speech bubble dot
    "-fill", "#facc15", "-draw", "circle 370,140 370,120",
    # Letter B overlay
    "-font", font_bold, "-pointsize", "140", "-fill", "#ffffff",
    "-gravity", "Center", "-annotate", "+0+30", "B",
    # Small subtitle
    "-font", font_bold, "-pointsize", "34", "-fill", "#22c55e",
    "-gravity", "South", "-annotate", "+0+75", "BAOBABBLE",
    "icon-512.png"
]
subprocess.run(cmd_icon, check=True)
print("icon-512.png generated successfully.")

# 2. Generate og-image.png (1200x630)
# Create base canvas with deep obsidian/emerald theme
cmd_og = [
    "convert", "-size", "1200x630", "xc:#0a0f0d",
    # Top border gradient accent
    "-fill", "#16a34a", "-draw", "rectangle 0,0 1200,10",
    "-fill", "#22c55e", "-draw", "rectangle 0,10 1200,14",
    # Decorative tribal / geometric border left and right
    "-fill", "#1e293b", "-draw", "rectangle 30,30 1170,600",
    "-fill", "#0f172a", "-draw", "rectangle 34,34 1166,596",
    # Glowing backdrop circle
    "-fill", "#14532d", "-draw", "circle 980,315 980,135",
    "-fill", "#166534", "-draw", "circle 980,315 980,180",
    "-fill", "#15803d", "-draw", "circle 980,315 980,240",
    # Baobab icon in the glow circle
    "-fill", "#ffffff",
    "-draw", "polygon 965,390 995,390 988,330 972,330",
    "-draw", "circle 980,300 980,265",
    "-draw", "circle 945,310 945,285",
    "-draw", "circle 1015,310 1015,285",
    "-fill", "#facc15", "-draw", "circle 1030,250 1030,238",
    # Left pill tag: "THE DUOLINGO FOR AFRICAN LANGUAGES"
    "-fill", "#16a34a", "-draw", "roundrectangle 70,70 540,115 16,16",
    "-font", font_bold, "-pointsize", "19", "-fill", "#ffffff",
    "-gravity", "NorthWest", "-annotate", "+95+83", "★  THE DUOLINGO FOR AFRICA",
    # Main App Title: Baobabble
    "-font", font_bold, "-pointsize", "84", "-fill", "#ffffff",
    "-gravity", "NorthWest", "-annotate", "+70+130", "Baobabble",
    # Accent dot
    "-font", font_bold, "-pointsize", "84", "-fill", "#22c55e",
    "-gravity", "NorthWest", "-annotate", "+575+130", ".",
    # Subtitle
    "-font", font_bold, "-pointsize", "34", "-fill", "#4ade80",
    "-gravity", "NorthWest", "-annotate", "+70+235", "Master 7 African Languages with AI",
    # Description
    "-font", font_regular, "-pointsize", "22", "-fill", "#94a3b8",
    "-gravity", "NorthWest", "-annotate", "+70+290", "Gamified lessons, native solfege tones & conversational AI tutor.",
    # Languages list pills
    "-font", font_bold, "-pointsize", "20", "-fill", "#cbd5e1",
    "-gravity", "NorthWest", "-annotate", "+70+330", "Yoruba  •  Igbo  •  Hausa  •  Swahili  •  Zulu  •  Amharic  •  Lingala",
    # Feature Badges
    "-fill", "#1e293b", "-draw", "roundrectangle 70,390 280,445 12,12",
    "-font", font_bold, "-pointsize", "18", "-fill", "#38bdf8",
    "-gravity", "NorthWest", "-annotate", "+95+406", "🤖  AI Live Tutor",
    "-fill", "#1e293b", "-draw", "roundrectangle 300,390 530,445 12,12",
    "-font", font_bold, "-pointsize", "18", "-fill", "#facc15",
    "-gravity", "NorthWest", "-annotate", "+325+406", "🎵  Native Tones",
    "-fill", "#1e293b", "-draw", "roundrectangle 550,390 770,445 12,12",
    "-font", font_bold, "-pointsize", "18", "-fill", "#a855f7",
    "-gravity", "NorthWest", "-annotate", "+575+406", "🎓  CEFR A1 to C2",
    # Call to action footer bar
    "-fill", "#16a34a", "-draw", "roundrectangle 70,490 420,555 14,14",
    "-font", font_bold, "-pointsize", "22", "-fill", "#ffffff",
    "-gravity", "NorthWest", "-annotate", "+100+507", "Start Learning Free  →",
    # Domain watermarking
    "-font", font_bold, "-pointsize", "22", "-fill", "#64748b",
    "-gravity", "NorthWest", "-annotate", "+460+514", "https://www.baobabble.com",
    "og-image.png"
]
subprocess.run(cmd_og, check=True)
print("og-image.png generated successfully.")

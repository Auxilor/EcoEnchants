package com.willfp.ecoenchants.enchantments;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.custom.CustomEcoEnchants;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.AngerArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.AshArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.CloudsArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.CopperArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.CrimsonArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.DamageArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.DragonArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.DustArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.EmeraldArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.EnchantmentArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.EndArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.FireArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.GlowArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.HeartArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.HoneyArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.InkArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.LavaArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.LimeArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.MagicArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.MagmaArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.MusicArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.NautilusArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.NetherArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.RainArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.RedstoneArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SlimeArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SmokeArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SnowArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SoulArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SoulFireArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SparkArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SparkleArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SporeArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.SweepArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.TearArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.TotemArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.VillagerArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.WarpedArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.WaterArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.WaxArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.WitchArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.artifact.ZapArtifact;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.BreaklessnessCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.CallingCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.DecayCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.FragilityCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.HarmlessnessCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.HungerCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.InaccuracyCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.MisfortuneCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.curse.PermanenceCurse;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Abattoir;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Abrasion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Aerial;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Aquatic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Arachnid;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Arborist;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Arcanic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Atmospheric;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Backstab;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Beheading;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.BlastMining;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Bleed;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Blind;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.BlockBreather;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.BossHunter;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Buckshot;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Butchering;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Cerebral;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Chopless;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Cleave;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Collateral;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Conclude;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Corrosive;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Cranial;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Criticals;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Cubism;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Defender;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Deflection;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Defusion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Dexterous;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Disable;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Disappear;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Diurnal;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Diverse;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Dousing;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Drill;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Dullness;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Dweller;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Economical;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Electroshock;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.EndInfusion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.EnderSlayer;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Enderism;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Evasion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Extinguishing;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Extract;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Famine;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Farmhand;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Fetching;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Finality;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Finishing;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.FireAffinity;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.FirstStrike;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Flinch;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Forcefield;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Freerunner;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Frozen;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Fury;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Goliath;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Graceful;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Grapple;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.GreenThumb;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Grit;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Hellish;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Hook;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Horde;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.IceShot;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Identify;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Ignite;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.IllusionAspect;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Impact;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Incandescence;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.InfernalTouch;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Inferno;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Infuriate;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Insecticide;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Instantaneous;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Introversion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Invigoration;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Kinetic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Launch;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Leeching;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Lesion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Levitate;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.LiquidShot;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.LuckyCatch;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Lumberjack;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Magnetic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Marking;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Marksman;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Necrotic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.NetherInfusion;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Netheric;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Nocturnal;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Optics;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Oxygenate;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Pacify;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Paladin;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Paralyze;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Parasitic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Parry;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Phantasm;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Plasmic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Protector;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Proximity;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Puncture;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Quadrilateralism;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Radiance;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Rage;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Rapid;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Reaper;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Rebounding;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Reel;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Reinforcement;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Rejuvenation;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Replenish;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Respirator;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Revenant;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Sating;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Serrated;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Settle;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Shockwave;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.ShotAssist;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Sickening;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Slaughter;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Slicing;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Spearfishing;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Spiked;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Splash;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Stab;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Stalwart;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Stamina;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.StoneSwitcher;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.StrayAspect;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Succession;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Supercritical;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Sycophant;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Tectonic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Telekinesis;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Thor;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Thrive;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Tornado;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Toxic;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Transfuse;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Tripleshot;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.VampireAspect;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Vein;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Venom;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.VoidAffinity;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Voltage;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.WaterAffinity;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.WaterAspect;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Weakening;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Wisdom;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.WoodSwitcher;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Wound;
import com.willfp.ecoenchants.enchantments.ecoenchants.normal.Zeus;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Aiming;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Annihilate;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Bladed;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Bolt;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Carve;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Confusion;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Energizing;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Force;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Frenzy;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Harpoon;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Indestructibility;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Instability;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Intellect;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.LifeSteal;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Pentashot;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Preservation;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Prosperity;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Razor;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Repairing;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Soulbound;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Spring;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Streamlining;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Volatile;
import com.willfp.ecoenchants.enchantments.ecoenchants.spell.Ascend;
import com.willfp.ecoenchants.enchantments.ecoenchants.spell.Charge;
import com.willfp.ecoenchants.enchantments.ecoenchants.spell.Dynamite;
import com.willfp.ecoenchants.enchantments.ecoenchants.spell.Missile;
import com.willfp.ecoenchants.enchantments.ecoenchants.spell.Quake;
import com.willfp.ecoenchants.enchantments.ecoenchants.spell.Vitalize;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.support.vanilla.VanillaEnchantments;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@UtilityClass
@SuppressWarnings({"unused", "checkstyle:JavadocVariable"})
public class EcoEnchants {
    public static final String CONFIG_LOCATION = "config.";
    public static final String OBTAINING_LOCATION = "obtaining.";
    public static final String GENERAL_LOCATION = "general-config.";

    private static final BiMap<NamespacedKey, EcoEnchant> BY_KEY = HashBiMap.create();
    private static final BiMap<String, EcoEnchant> BY_NAME = HashBiMap.create();

    public static final EcoEnchant TELEKINESIS = new Telekinesis();
    public static final EcoEnchant MARKSMAN = new Marksman();
    public static final EcoEnchant INFERNAL_TOUCH = new InfernalTouch();
    public static final EcoEnchant SPRING = new Spring();
    public static final EcoEnchant STRAY_ASPECT = new StrayAspect();
    public static final EcoEnchant ILLUSION_ASPECT = new IllusionAspect();
    public static final EcoEnchant SLICING = new Slicing();
    public static final EcoEnchant DEXTEROUS = new Dexterous();
    public static final EcoEnchant BEHEADING = new Beheading();
    public static final EcoEnchant NECROTIC = new Necrotic();
    public static final EcoEnchant TECTONIC = new Tectonic();
    public static final EcoEnchant EVASION = new Evasion();
    public static final EcoEnchant SUCCESSION = new Succession();
    public static final EcoEnchant FARMHAND = new Farmhand();
    public static final EcoEnchant WISDOM = new Wisdom();
    public static final EcoEnchant LEECHING = new Leeching();
    public static final EcoEnchant VAMPIRE_ASPECT = new VampireAspect();
    public static final EcoEnchant INSTABILITY = new Instability();
    public static final EcoEnchant THRIVE = new Thrive();
    public static final EcoEnchant DRILL = new Drill();
    public static final EcoEnchant THOR = new Thor();
    public static final EcoEnchant STREAMLINING = new Streamlining();
    public static final EcoEnchant FIRST_STRIKE = new FirstStrike();
    public static final EcoEnchant FINISHING = new Finishing();
    public static final EcoEnchant CRITICALS = new Criticals();
    public static final EcoEnchant INCANDESCENCE = new Incandescence();
    public static final EcoEnchant SUPERCRITICAL = new Supercritical();
    public static final EcoEnchant ABRASION = new Abrasion();
    public static final EcoEnchant SPLASH = new Splash();
    public static final EcoEnchant EXTINGUISHING = new Extinguishing();
    public static final EcoEnchant GOLIATH = new Goliath();
    public static final EcoEnchant OPTICS = new Optics();
    public static final EcoEnchant DEFUSION = new Defusion();
    public static final EcoEnchant CEREBRAL = new Cerebral();
    public static final EcoEnchant GRIT = new Grit();
    public static final EcoEnchant BOSS_HUNTER = new BossHunter();
    public static final EcoEnchant INVIGORATION = new Invigoration();
    public static final EcoEnchant REJUVENATION = new Rejuvenation();
    public static final EcoEnchant FRAGILITY_CURSE = new FragilityCurse();
    public static final EcoEnchant TRIPLESHOT = new Tripleshot();
    public static final EcoEnchant RAPID = new Rapid();
    public static final EcoEnchant SATING = new Sating();
    public static final EcoEnchant REINFORCEMENT = new Reinforcement();
    public static final EcoEnchant SOULBOUND = new Soulbound();
    public static final EcoEnchant RAZOR = new Razor();
    public static final EcoEnchant PROSPERITY = new Prosperity();
    public static final EcoEnchant PRESERVATION = new Preservation();
    public static final EcoEnchant FRENZY = new Frenzy();
    public static final EcoEnchant BUTCHERING = new Butchering();
    public static final EcoEnchant PROXIMITY = new Proximity();
    public static final EcoEnchant ENDER_SLAYER = new EnderSlayer();
    public static final EcoEnchant PROTECTOR = new Protector();
    public static final EcoEnchant INDESTRUCTIBILITY = new Indestructibility();
    public static final EcoEnchant ENERGIZING = new Energizing();
    public static final EcoEnchant INTELLECT = new Intellect();
    public static final EcoEnchant DEFLECTION = new Deflection();
    public static final EcoEnchant LAUNCH = new Launch();
    public static final EcoEnchant PERMANENCE_CURSE = new PermanenceCurse();
    public static final EcoEnchant SPEARFISHING = new Spearfishing();
    public static final EcoEnchant NETHER_INFUSION = new NetherInfusion();
    public static final EcoEnchant REPLENISH = new Replenish();
    public static final EcoEnchant FLINCH = new Flinch();
    public static final EcoEnchant ELECTROSHOCK = new Electroshock();
    public static final EcoEnchant NOCTURNAL = new Nocturnal();
    public static final EcoEnchant CONFUSION = new Confusion();
    public static final EcoEnchant ARCANIC = new Arcanic();
    public static final EcoEnchant PENTASHOT = new Pentashot();
    public static final EcoEnchant LUMBERJACK = new Lumberjack();
    public static final EcoEnchant STONE_SWITCHER = new StoneSwitcher();
    public static final EcoEnchant MAGNETIC = new Magnetic();
    public static final EcoEnchant REPAIRING = new Repairing();
    public static final EcoEnchant CALLING_CURSE = new CallingCurse();
    public static final EcoEnchant BLAST_MINING = new BlastMining();
    public static final EcoEnchant LIQUID_SHOT = new LiquidShot();
    public static final EcoEnchant GRAPPLE = new Grapple();
    public static final EcoEnchant HEART_ARTIFACT = new HeartArtifact();
    public static final EcoEnchant SPARKLE_ARTIFACT = new SparkleArtifact();
    public static final EcoEnchant LAVA_ARTIFACT = new LavaArtifact();
    public static final EcoEnchant DRAGON_ARTIFACT = new DragonArtifact();
    public static final EcoEnchant ENCHANTMENT_ARTIFACT = new EnchantmentArtifact();
    public static final EcoEnchant SMOKE_ARTIFACT = new SmokeArtifact();
    public static final EcoEnchant FIRE_ARTIFACT = new FireArtifact();
    public static final EcoEnchant EMERALD_ARTIFACT = new EmeraldArtifact();
    public static final EcoEnchant NETHER_ARTIFACT = new NetherArtifact();
    public static final EcoEnchant END_ARTIFACT = new EndArtifact();
    public static final EcoEnchant WATER_ARTIFACT = new WaterArtifact();
    public static final EcoEnchant TOTEM_ARTIFACT = new TotemArtifact();
    public static final EcoEnchant REDSTONE_ARTIFACT = new RedstoneArtifact();
    public static final EcoEnchant ZAP_ARTIFACT = new ZapArtifact();
    public static final EcoEnchant MUSIC_ARTIFACT = new MusicArtifact();
    public static final EcoEnchant SNOW_ARTIFACT = new SnowArtifact();
    public static final EcoEnchant WITCH_ARTIFACT = new WitchArtifact();
    public static final EcoEnchant HONEY_ARTIFACT = new HoneyArtifact();
    public static final EcoEnchant DAMAGE_ARTIFACT = new DamageArtifact();
    public static final EcoEnchant CLOUDS_ARTIFACT = new CloudsArtifact();
    public static final EcoEnchant MAGIC_ARTIFACT = new MagicArtifact();
    public static final EcoEnchant DUST_ARTIFACT = new DustArtifact();
    public static final EcoEnchant MAGMA_ARTIFACT = new MagmaArtifact();
    public static final EcoEnchant INK_ARTIFACT = new InkArtifact();
    public static final EcoEnchant ZEUS = new Zeus();
    public static final EcoEnchant KINETIC = new Kinetic();
    public static final EcoEnchant FIRE_AFFINITY = new FireAffinity();
    public static final EcoEnchant PARASITIC = new Parasitic();
    public static final EcoEnchant PARRY = new Parry();
    public static final EcoEnchant AIMING = new Aiming();
    public static final EcoEnchant HOOK = new Hook();
    public static final EcoEnchant BLEED = new Bleed();
    public static final EcoEnchant WEAKENING = new Weakening();
    public static final EcoEnchant OXYGENATE = new Oxygenate();
    public static final EcoEnchant WATER_ASPECT = new WaterAspect();
    public static final EcoEnchant STAMINA = new Stamina();
    public static final EcoEnchant COLLATERAL = new Collateral();
    public static final EcoEnchant HUNGER_CURSE = new HungerCurse();
    public static final EcoEnchant PALADIN = new Paladin();
    public static final EcoEnchant SERRATED = new Serrated();
    public static final EcoEnchant BLADED = new Bladed();
    public static final EcoEnchant INFERNO = new Inferno();
    public static final EcoEnchant STAB = new Stab();
    public static final EcoEnchant TORNADO = new Tornado();
    public static final EcoEnchant EXTRACT = new Extract();
    public static final EcoEnchant AERIAL = new Aerial();
    public static final EcoEnchant FAMINE = new Famine();
    public static final EcoEnchant ANNIHILATE = new Annihilate();
    public static final EcoEnchant RADIANCE = new Radiance();
    public static final EcoEnchant HORDE = new Horde();
    public static final EcoEnchant VEIN = new Vein();
    public static final EcoEnchant ICE_SHOT = new IceShot();
    public static final EcoEnchant PUNCTURE = new Puncture();
    public static final EcoEnchant SHOCKWAVE = new Shockwave();
    public static final EcoEnchant VOLATILE = new Volatile();
    public static final EcoEnchant INSTANTANEOUS = new Instantaneous();
    public static final EcoEnchant FREERUNNER = new Freerunner();
    public static final EcoEnchant BOLT = new Bolt();
    public static final EcoEnchant DULLNESS = new Dullness();
    public static final EcoEnchant IGNITE = new Ignite();
    public static final EcoEnchant CLEAVE = new Cleave();
    public static final EcoEnchant CARVE = new Carve();
    public static final EcoEnchant TOXIC = new Toxic();
    public static final EcoEnchant WATER_AFFINITY = new WaterAffinity();
    public static final EcoEnchant FORCEFIELD = new Forcefield();
    public static final EcoEnchant SYCOPHANT = new Sycophant();
    public static final EcoEnchant CHOPLESS = new Chopless();
    public static final EcoEnchant GREEN_THUMB = new GreenThumb();
    public static final EcoEnchant SPIKED = new Spiked();
    public static final EcoEnchant HARPOON = new Harpoon();
    public static final EcoEnchant REEL = new Reel();
    public static final EcoEnchant SHOT_ASSIST = new ShotAssist();
    public static final EcoEnchant FROZEN = new Frozen();
    public static final EcoEnchant DISAPPEAR = new Disappear();
    public static final EcoEnchant HARMLESSNESS_CURSE = new HarmlessnessCurse();
    public static final EcoEnchant FURY = new Fury();
    public static final EcoEnchant LEVITATE = new Levitate();
    public static final EcoEnchant BREAKLESSNESS_CURSE = new BreaklessnessCurse();
    public static final EcoEnchant DECAY_CURSE = new DecayCurse();
    public static final EcoEnchant MISFORTUNE_CURSE = new MisfortuneCurse();
    public static final EcoEnchant VENOM = new Venom();
    public static final EcoEnchant CRANIAL = new Cranial();
    public static final EcoEnchant AQUATIC = new Aquatic();
    public static final EcoEnchant BUCKSHOT = new Buckshot();
    public static final EcoEnchant DIVERSE = new Diverse();
    public static final EcoEnchant LIFE_STEAL = new LifeSteal();
    public static final EcoEnchant LIME_ARTIFACT = new LimeArtifact();
    public static final EcoEnchant FORCE = new Force();
    public static final EcoEnchant END_INFUSION = new EndInfusion();
    public static final EcoEnchant DIURNAL = new Diurnal();
    public static final EcoEnchant MARKING = new Marking();
    public static final EcoEnchant CORROSIVE = new Corrosive();
    public static final EcoEnchant WOUND = new Wound();
    public static final EcoEnchant FINALITY = new Finality();
    public static final EcoEnchant BLIND = new Blind();
    public static final EcoEnchant SICKENING = new Sickening();
    public static final EcoEnchant DEFENDER = new Defender();
    public static final EcoEnchant NETHERIC = new Netheric();
    public static final EcoEnchant ENDERISM = new Enderism();
    public static final EcoEnchant RAGE = new Rage();
    public static final EcoEnchant IMPACT = new Impact();
    public static final EcoEnchant PARALYZE = new Paralyze();
    public static final EcoEnchant IDENTIFY = new Identify();
    public static final EcoEnchant INFURIATE = new Infuriate();
    public static final EcoEnchant ATMOSPHERIC = new Atmospheric();
    public static final EcoEnchant REVENANT = new Revenant();
    public static final EcoEnchant INSECTICIDE = new Insecticide();
    public static final EcoEnchant SLAUGHTER = new Slaughter();
    public static final EcoEnchant SETTLE = new Settle();
    public static final EcoEnchant PHANTASM = new Phantasm();
    public static final EcoEnchant ARACHNID = new Arachnid();
    public static final EcoEnchant PACIFY = new Pacify();
    public static final EcoEnchant ABATTOIR = new Abattoir();
    public static final EcoEnchant DISABLE = new Disable();
    public static final EcoEnchant HELLISH = new Hellish();
    public static final EcoEnchant VOID_AFFINITY = new VoidAffinity();
    public static final EcoEnchant CUBISM = new Cubism();
    public static final EcoEnchant QUADRILATERALISM = new Quadrilateralism();
    public static final EcoEnchant LESION = new Lesion();
    public static final EcoEnchant CONCLUDE = new Conclude();
    public static final EcoEnchant GRACEFUL = new Graceful();
    public static final EcoEnchant BLOCK_BREATHER = new BlockBreather();
    public static final EcoEnchant VOLTAGE = new Voltage();
    public static final EcoEnchant TRANSFUSE = new Transfuse();
    public static final EcoEnchant INACCURACY_CURSE = new InaccuracyCurse();
    public static final EcoEnchant RESPIRATOR = new Respirator();
    public static final EcoEnchant FETCHING = new Fetching();
    public static final EcoEnchant ECONOMICAL = new Economical();
    public static final EcoEnchant SOUL_ARTIFACT = new SoulArtifact();
    public static final EcoEnchant SOUL_FIRE_ARTIFACT = new SoulFireArtifact();
    public static final EcoEnchant CRIMSON_ARTIFACT = new CrimsonArtifact();
    public static final EcoEnchant ASH_ARTIFACT = new AshArtifact();
    public static final EcoEnchant WARPED_ARTIFACT = new WarpedArtifact();
    public static final EcoEnchant TEAR_ARTIFACT = new TearArtifact();
    public static final EcoEnchant BACKSTAB = new Backstab();
    public static final EcoEnchant DWELLER = new Dweller();
    public static final EcoEnchant STALWART = new Stalwart();
    public static final EcoEnchant PLASMIC = new Plasmic();
    public static final EcoEnchant MISSILE = new Missile();
    public static final EcoEnchant QUAKE = new Quake();
    public static final EcoEnchant VITALIZE = new Vitalize();
    public static final EcoEnchant DYNAMITE = new Dynamite();
    public static final EcoEnchant CHARGE = new Charge();
    public static final EcoEnchant ASCEND = new Ascend();
    public static final EcoEnchant ARBORIST = new Arborist();
    public static final EcoEnchant LUCKY_CATCH = new LuckyCatch();
    public static final EcoEnchant INTROVERSION = new Introversion();
    public static final EcoEnchant VILLAGER_ARTIFACT = new VillagerArtifact();
    public static final EcoEnchant ANGER_ARTIFACT = new AngerArtifact();
    public static final EcoEnchant NAUTILUS_ARTIFACT = new NautilusArtifact();
    public static final EcoEnchant SWEEP_ARTIFACT = new SweepArtifact();
    public static final EcoEnchant REAPER = new Reaper();
    public static final EcoEnchant WOOD_SWITCHER = new WoodSwitcher();
    public static final EcoEnchant REBOUNDING = new Rebounding();
    public static final EcoEnchant COPPER_ARTIFACT = new CopperArtifact();
    public static final EcoEnchant GLOW_ARTIFACT = new GlowArtifact();
    public static final EcoEnchant SPARK_ARTIFACT = new SparkArtifact();
    public static final EcoEnchant SPORE_ARTIFACT = new SporeArtifact();
    public static final EcoEnchant WAX_ARTIFACT = new WaxArtifact();
    public static final EcoEnchant RAIN_ARTIFACT = new RainArtifact();
    public static final EcoEnchant SLIME_ARTIFACT = new SlimeArtifact();
    public static final EcoEnchant DOUSING = new Dousing();

    /**
     * Get all registered {@link EcoEnchant}s.
     *
     * @return A list of all {@link EcoEnchant}s.
     */
    public static List<EcoEnchant> values() {
        return ImmutableList.copyOf(BY_KEY.values());
    }

    /**
     * Get {@link EcoEnchant} matching display name.
     *
     * @param name The display name to search for.
     * @return The matching {@link EcoEnchant}, or null if not found.
     */
    public static EcoEnchant getByName(@NotNull final String name) {
        return BY_NAME.get(name);
    }

    /**
     * Get {@link EcoEnchant} matching permission name.
     *
     * @param permissionName The permission name to search for.
     * @return The matching {@link EcoEnchant}, or null if not found.
     */
    public static EcoEnchant getByPermission(@NotNull final String permissionName) {
        Optional<EcoEnchant> matching = values().stream().filter(enchant -> enchant.getPermissionName().equalsIgnoreCase(permissionName)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Get {@link EcoEnchant} matching key.
     *
     * @param key The NamespacedKey to search for.
     * @return The matching {@link EcoEnchant}, or null if not found.
     */
    public static EcoEnchant getByKey(@NotNull final NamespacedKey key) {
        return BY_KEY.get(key);
    }

    /**
     * Get if {@link ItemStack} has any {@link EcoEnchant} matching specified {@link EnchantmentType}.
     *
     * @param item The {@link ItemStack} to check.
     * @param type The {@link EnchantmentType} to match.
     * @return True if has, false if doesn't have.
     */
    public static boolean hasAnyOfType(@NotNull final ItemStack item,
                                       @NotNull final EnchantmentType type) {
        return FastItemStack.wrap(item).getEnchantmentsOnItem(true).keySet()
                .stream()
                .filter(enchantment -> enchantment instanceof EcoEnchant)
                .map(enchantment -> (EcoEnchant) enchantment)
                .anyMatch(ecoEnchant -> ecoEnchant.getType().equals(type));
    }

    /**
     * Update all {@link EcoEnchant}s.
     *
     * @param plugin Instance of EcoEnchants.
     */
    @ConfigUpdater
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        CustomEcoEnchants.update(plugin);

        for (EcoEnchant ecoEnchant : new HashSet<>(values())) {
            ecoEnchant.update();
        }

        VanillaEnchantments.update(plugin);
    }

    /**
     * Add new {@link EcoEnchant} to EcoEnchants.
     * <p>
     * Only for internal use, enchantments are automatically added in the constructor.
     *
     * @param enchant The {@link EcoEnchant} to add.
     */
    public static void addNewEcoEnchant(@NotNull final EcoEnchant enchant) {
        BY_KEY.remove(enchant.getKey());
        BY_NAME.inverse().remove(enchant);
        BY_KEY.put(enchant.getKey(), enchant);
        BY_NAME.put(enchant.getDisplayName(), enchant);
    }

    /**
     * Remove {@link EcoEnchant} from EcoEnchants.
     *
     * @param enchant The {@link EcoEnchant} to remove.
     */
    public static void removeEcoEnchant(@NotNull final EcoEnchant enchant) {
        BY_KEY.remove(enchant.getKey());
        BY_NAME.inverse().remove(enchant);
    }
}

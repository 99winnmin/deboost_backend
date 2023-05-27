package com.samnamja.deboost.api.dto.openfeign.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameAllDetailInfoResponseDto {
    private MetadataDto metadata;
    private InfoDTO info;

    @Getter
    public static class MetadataDto {
        private String dataVersion;
        private String matchId;
        private List<String> participants;
    }

    @Getter
    public static class InfoDTO {
        private long gameCreation;
        private int gameDuration;
        private long gameEndTimestamp;
        private long gameId;
        private String gameMode;
        private String gameName;
        private long gameStartTimestamp;
        private String gameType;
        private String gameVersion;
        private int mapId;
        private String platformId;
        private int queueId;
        private String tournamentCode;
        private List<ParticipantDTO> participants;
        private List<TeamDTD> teams;

        @Getter
        public static class ParticipantDTO {
            private int assists;
            private int baronKills;
            private int bountyLevel;
            private GameChallengesDTO challenges;
            private int champExperience;
            private int champLevel;
            private int championId;
            private String championName;
            private int championTransform;
            private int consumablesPurchased;
            private int damageDealtToBuildings;
            private int damageDealtToObjectives;
            private int damageDealtToTurrets;
            private int damageSelfMitigated;
            private int deaths;
            private int detectorWardsPlaced;
            private int doubleKills;
            private int dragonKills;
            private boolean eligibleForProgression;
            private boolean firstBloodAssist;
            private boolean firstBloodKill;
            private boolean firstTowerAssist;
            private boolean firstTowerKill;
            private boolean gameEndedInEarlySurrender;
            private boolean gameEndedInSurrender;
            private int goldEarned;
            private int goldSpent;
            private String individualPosition;
            private int inhibitorKills;
            private int inhibitorTakedowns;
            private int inhibitorsLost;
            private int item0;
            private int item1;
            private int item2;
            private int item3;
            private int item4;
            private int item5;
            private int item6;
            private int itemsPurchased;
            private int killingSprees;
            private int kills;
            private String lane;
            private int largestCriticalStrike;
            private int largestKillingSpree;
            private int largestMultiKill;
            private int longestTimeSpentLiving;
            private int magicDamageDealt;
            private int magicDamageDealtToChampions;
            private int magicDamageTaken;
            private int neutralMinionsKilled;
            private int nexusKills;
            private int nexusLost;
            private int nexusTakedowns;
            private int objectivesStolen;
            private int objectivesStolenAssists;
            private int participantId;
            private int pentaKills;
            private PerksDto perks;
            private int physicalDamageDealt;
            private int physicalDamageDealtToChampions;
            private int physicalDamageTaken;
            private int profileIcon;
            private String puuid;
            private int quadraKills;
            private String riotIdName;
            private String riotIdTagline;
            private String role;
            private int sightWardsBoughtInGame;
            private int spell1Casts;
            private int spell2Casts;
            private int spell3Casts;
            private int spell4Casts;
            private int summoner1Casts;
            private int summoner1Id;
            private int summoner2Casts;
            private int summoner2Id;
            private String summonerId;
            private int summonerLevel;
            private String summonerName;
            private boolean teamEarlySurrendered;
            private int teamId;
            private String teamPosition;
            private int timeCCingOthers;
            private int timePlayed;
            private int totalDamageDealt;
            private int totalDamageDealtToChampions;
            private int totalDamageShieldedOnTeammates;
            private int totalDamageTaken;
            private int totalHeal;
            private int totalHealsOnTeammates;
            private int totalMinionsKilled;
            private int totalTimeCCDealt;
            private int totalTimeSpentDead;
            private int totalUnitsHealed;
            private int tripleKills;
            private int trueDamageDealt;
            private int trueDamageDealtToChampions;
            private int trueDamageTaken;
            private int turretKills;
            private int turretTakedowns;
            private int turretsLost;
            private int unrealKills;
            private int visionScore;
            private int visionWardsBoughtInGame;
            private int wardsKilled;
            private int wardsPlaced;
            private boolean win;

            @Getter
            public static class GameChallengesDTO {
                private int assistStreakCount;
                private int abilityUses;
                private int acesBefore15Minutes;
                private double alliedJungleMonsterKills;
                private int baronTakedowns;
                private int blastConeOppositeOpponentCount;
                private int bountyGold;
                private int buffsStolen;
                private int completeSupportQuestInTime;
                private double controlWardTimeCoverageInRiverOrEnemyHalf;
                private int controlWardsPlaced;
                private double damagePerMinute;
                private double damageTakenOnTeamPercentage;
                private int dancedWithRiftHerald;
                private int deathsByEnemyChamps;
                private int dodgeSkillShotsSmallWindow;
                private int doubleAces;
                private int dragonTakedowns;
                private int earlyLaningPhaseGoldExpAdvantage;
                private int effectiveHealAndShielding;
                private int elderDragonKillsWithOpposingSoul;
                private int elderDragonMultikills;
                private int enemyChampionImmobilizations;
                private double enemyJungleMonsterKills;
                private int epicMonsterKillsNearEnemyJungler;
                private int epicMonsterKillsWithin30SecondsOfSpawn;
                private int epicMonsterSteals;
                private int epicMonsterStolenWithoutSmite;
                private int flawlessAces;
                private int fullTeamTakedown;
                private double gameLength;
                private int getTakedownsInAllLanesEarlyJungleAsLaner;
                private double goldPerMinute;
                private int hadAfkTeammate;
                private int hadOpenNexus;
                private int immobilizeAndKillWithAlly;
                private int initialBuffCount;
                private int initialCrabCount;
                private int jungleCsBefore10Minutes;
                private int junglerTakedownsNearDamagedEpicMonster;
                private int kturretsDestroyedBeforePlatesFall;
                private double kda;
                private int killAfterHiddenWithAlly;
                private double killParticipation;
                private int killedChampTookFullTeamDamageSurvived;
                private int killsNearEnemyTurret;
                private int killsOnOtherLanesEarlyJungleAsLaner;
                private int killsOnRecentlyHealedByAramPack;
                private int killsUnderOwnTurret;
                private int killsWithHelpFromEpicMonster;
                private int knockEnemyIntoTeamAndKill;
                private int landSkillShotsEarlyGame;
                private int laneMinionsFirst10Minutes;
                private int laningPhaseGoldExpAdvantage;
                private int legendaryCount;
                private int lostAnInhibitor;
                private int maxCsAdvantageOnLaneOpponent;
                private int maxKillDeficit;
                private int maxLevelLeadLaneOpponent;
                private double moreEnemyJungleThanOpponent;
                private int multiKillOneSpell;
                private int multiTurretRiftHeraldCount;
                private int multikills;
                private int multikillsAfterAggressiveFlash;
                private int mythicItemUsed;
                private int outerTurretExecutesBefore10Minutes;
                private int outnumberedKills;
                private int outnumberedNexusKill;
                private int perfectDragonSoulsTaken;
                private int perfectGame;
                private int pickKillWithAlly;
                private int poroExplosions;
                private int quickCleanse;
                private int quickFirstTurret;
                private int quickSoloKills;
                private int riftHeraldTakedowns;
                private int saveAllyFromDeath;
                private int scuttleCrabKills;
                private int skillshotsDodged;
                private int skillshotsHit;
                private int snowballsHit;
                private int soloBaronKills;
                private int soloKills;
                private int soloTurretsLategame;
                private int stealthWardsPlaced;
                private int survivedSingleDigitHpCount;
                private int survivedThreeImmobilizesInFight;
                private int takedownOnFirstTurret;
                private int takedowns;
                private int takedownsAfterGainingLevelAdvantage;
                private int takedownsBeforeJungleMinionSpawn;
                private int takedownsFirstXMinutes;
                private int takedownsInAlcove;
                private int takedownsInEnemyFountain;
                private int teamBaronKills;
                private double teamDamagePercentage;
                private int teamElderDragonKills;
                private int teamRiftHeraldKills;
                private int threeWardsOneSweeperCount;
                private int tookLargeDamageSurvived;
                private int turretPlatesTaken;
                private int turretTakedowns;
                private int turretsTakenWithRiftHerald;
                private int twentyMinionsIn3SecondsCount;
                private int unseenRecalls;
                private double visionScoreAdvantageLaneOpponent;
                private double visionScorePerMinute;
                private int wardTakedowns;
                private int wardTakedownsBefore20M;
                private int wardsGuarded;
            }
            @Getter
            public static class PerksDto {
                private StatPerksDTO statPerks;
                private List<StyleDTO> styles;

                @Getter
                public static class StatPerksDTO {
                    private int defense;
                    private int flex;
                    private int offense;
                }

                @Getter
                public static class StyleDTO {
                    private String description;
                    private List<SelectionDTO> selections;
                    private int style;

                    @Getter
                    public static class SelectionDTO {
                        private int perk;
                        private int var1;
                        private int var2;
                        private int var3;
            }
        }
    }
        }

        @Getter
        public static class TeamDTD {
            private List<BanDTO> bans;
            private ObjectivesDTO objectives;
            private int teamId;
            private boolean win;

            @Getter
            public static class BanDTO {
                private int championId;
                private int pickTurn;
            }

            @Getter
            public static class ObjectivesDTO {
                private ObjectiveDTO baron;
                private ObjectiveDTO champion;
                private ObjectiveDTO dragon;
                private ObjectiveDTO inhibitor;
                private ObjectiveDTO riftHerald;
                private ObjectiveDTO tower;

                @Getter
                public static class ObjectiveDTO {
                    private boolean first;
                    private int kills;
                }
            }
        }
    }
}

# How to contribute to EcoEnchants

## Codestyle

1. The EcoEnchants checkstyle is in /config/checkstyle.xml

- The pull request must not have any checkstyle issues.
- Every method and field must have a javadoc attached.

2. Use lombok wherever possible.

- As of 6.0.0, EcoEnchants is now built with lombok.
- @Getter, @Setter, @ToString, @EqualsAndHashCode, @UtilityClass are the most important.

3. Use JetBrains annotations

- Every parameter should be annotated with @NotNull or @Nullable
- Use @NotNull over lombok @NonNull

4. Imports

- No group (*) imports.
- No static imports.

## Dependency Injection

- EcoEnchants uses Dependency Injection since 6.0.0.
- Any calls to AbstractEcoPlugin#getInstance are code smells and should never be used unless **absolutely necessary**.
- NamespacedKeys, FixedMetadataValues, Runnables, and Schedules should be managed using EcoEnchantsPlugin through DI.
- Any DI class should extend PluginDependent where possible. If the class extends another, then you **must** store the
  plugin instance in a private final variable called **plugin** with a private or protected getter.

## Other

- All drops **must** be sent through a DropQueue - calls to World#dropItem will get your PR rejected.
- Any non-plugin-specific changes **must** be made to eco-util, or core-proxy, rather than core-plugin.
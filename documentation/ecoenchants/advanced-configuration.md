---
title: "Advanced Configuration"
sidebar_position: 3
---

## Cost Exponent

Cost exponent is a feature of anvils, which can increase or decrease cost based on the original cost.

The formula works as follows:

```go
cost = level^exponent + 1
```

So, working with an exponent of `1.02` and an original cost of `25`:

```go
cost = 25^1.02 + 1
```

This is then **rounded up** to the nearest whole number, so the cost in this example would become **28**. This formula ensures cost scaling stays fair, fun and consistent across all levels.
#### Configuration Tips

Set the `cost-exponent` in your config to control difficulty:

- `0.8` → Easy
- `0.9` → Balanced
- `1.0` → Vanilla-like
- `1.2` → Harder
## Enchantment Type Bias

You might design some enchantment types (e.g. special enchantments) to be extremely rare, and require a lot of work to balance out their power.

To do this, you can bias enchantment levels according to a curve.

Let's use an enchantment called Razor for this example.

By default, Razor has 5 Levels. So, to calculate the level to apply, a random number between 0 and 1 is generated. This number is then biased according to a curve, which means that more inputs give a lower output, so for example 0.7 may become 0.1, and only extremely high inputs, such as 0.99 may become 0.6 or higher.

The "band" for each level is calculated by dividing 1 by the amount of levels. This looks like this for an enchantment with 5 levels:

| Level | Range      |
| ----- | ---------- |
| 1     | 0 - 0.2    |
| 2     | 0.21 - 0.4 |
| 3     | 0.41 - 0.6 |
| 4     | 0.61 - 0.8 |
| 5     | 0.81 - 1   |

Increasing the bias towards 1 will lead to an increased likeliness of low-level enchantments, and lowering the bias towards -1 will lead to an increased likeliness of high-level enchantments.

package com.agoda.boots.strict

import com.agoda.boots.Bootable
import com.agoda.boots.Key

/**
 * This class is used to determine ICC (Incorrect connected components) in the
 * [Bootable]'s dependency tree every time you invoke [add()][com.agoda.boots.Boots.add].
 *
 * As of now, ICC is considered to be:
 * - Bootable with [Bootable.isCritical] flag set to `true` which is dependent
 *   on a bootable with [Bootable.isCritical] flag set to `false`
 *
 * @param boots list of bootables to process
 */
class IccFinder(private val boots: List<Bootable>) {

    /**
     * Determines if given [Bootable] set contains ICC
     * through it's dependency tree.
     *
     * @return Set of ICCs each represented by Pair<[Key], [Key]>
     */
    fun find(): List<Pair<Key, Key>> {
        val results = mutableListOf<Pair<Key, Key>>()

        boots.filter {
            it.isCritical
        }.forEach { critical ->
            critical.dependencies.forEach { key ->
                val dependency = boots.find { it.key == key }!!

                if (!dependency.isCritical) {
                    results.add(critical.key to key)
                }
            }
        }

        return results
    }

}

package com.mds.comm.enumerator.pattern;

/**
 * Contract for enumeration entries that carry a display order and a
 * human-readable description.
 *
 * @author MDS
 * @since 0.0.1-SNAPSHOT
 */
public interface EnumerationPattern {

  /**
   * Returns the display order of this entry.
   *
   * @return the numeric order
   */
  int getOrder();

  /**
   * Returns the human-readable description of this entry.
   *
   * @return the description
   */
  String getDescription();
}

package org.daisy.dotify.api.formatter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides global settings for a formatter.
 * @author Joel Håkansson
 *
 */
public class FormatterConfiguration {
	private final String translationMode;
	private final String locale;
	private final boolean allowsTextOverflow;
	private final boolean hyphenating;
	private final boolean marksCapitalLetters;
	private final Set<String> ignoredStyles;

	/**
	 * Provides a builder for formatter configuration
	 * @author Joel Håkansson
	 */
	public static class Builder {
		private final String translationMode;
		private final String locale;
		private boolean allowsTextOverflow = false;
		private boolean hyphenating = true;
		private boolean marksCapitalLetters = true;
		private Set<String> ignoredStyles = new HashSet<String>();
		
		/**
		 * Creates a new builder with the specified properties
		 * @param locale the locale
		 * @param mode the braille translation mode
		 */
		public Builder(String locale, String mode) {
			this.translationMode = mode;
			this.locale = locale;
		}
		/**
		 * Sets the text overflow policy. If the value is true, text that overflows 
		 * its boundaries may be truncated if needed. If the value is false, an 
		 * error should be thrown and the process aborted (default).
		 * 
		 * @param value the value of the text overflow policy
		 * @return returns this builder
		 */
		public Builder allowsTextOverflow(boolean value) {
			this.allowsTextOverflow = value;
			return this;
		}
		/**
		 * Sets the global hyphenation policy
		 * @param value the value of the global hyphenation policy
		 * @return returns this builder
		 */
		public Builder hyphenate(boolean value) {
			hyphenating = value;
			return this;
		}

		/**
		 * Sets the global capital letter policy
		 * @param value the value of the capital letters policy
		 * @return returns this builder
		 */
		public Builder markCapitalLetters(boolean value) {
			marksCapitalLetters = value;
			return this;
		}
		
		/**
		 * Adds a style to ignore
		 * @param style a style to ignore
		 * @return returns this builder
		 */
		public Builder ignoreStyle(String style) {
			ignoredStyles.add(style);
			return this;
		}
		
		/**
		 * Creates new configuration
		 * @return returns a new configuration instance
		 */
		public FormatterConfiguration build() {
			return new FormatterConfiguration(this);
		}

	}
	
	/**
	 * Creates a new builder with the specified properties
	 * @param locale the locale
	 * @param mode the braille translation mode
	 * @return returns a new builder
	 */
	public static Builder with(String locale, String mode) {
		return new Builder(locale, mode);
	}

	private FormatterConfiguration(Builder builder) {
		locale = builder.locale;
		translationMode = builder.translationMode;
		allowsTextOverflow = builder.allowsTextOverflow;
		hyphenating = builder.hyphenating;
		marksCapitalLetters = builder.marksCapitalLetters;
		ignoredStyles = Collections.unmodifiableSet(new HashSet<>(builder.ignoredStyles));
	}

	/**
	 * Gets the translation mode
	 * @return returns the translation mode
	 */
	public String getTranslationMode() {
		return translationMode;
	}
	
	/**
	 * Gets the locale
	 * @return returns the locale
	 */
	public String getLocale() {
		return locale;
	}
	
	public boolean isAllowingTextOverflow() {
		return allowsTextOverflow;
	}

	/**
	 * Returns true if the formatter is hyphenating
	 * @return returns true if the formatter is hyphenating, false otherwise
	 */
	public boolean isHyphenating() {
		return hyphenating;
	}
	
	/**
	 * Returns true if capital letters should be marked, false otherwise
	 * @return returns true if capital letters should be marked, false otherwise
	 */
	public boolean isMarkingCapitalLetters() {
		return marksCapitalLetters;
	}
	
	/**
	 * Gets a set of ignored styles
	 * @return returns the set of ignored styles
	 */
	public Set<String> getIgnoredStyles() {
		return ignoredStyles;
	}

}

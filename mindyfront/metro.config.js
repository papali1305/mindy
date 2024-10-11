// metro.config.js
const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

// Get the default Metro configuration
const defaultConfig = getDefaultConfig(__dirname);
const { assetExts, sourceExts } = defaultConfig.resolver;

/**
 * Metro configuration
 * https://facebook.github.io/metro/docs/configuration
 *
 * @type {import('metro-config').MetroConfig}
 */
const config = {
  transformer: {
    // Allow require.context to be used
    unstable_allowRequireContext: true,
    // Use the SVG transformer for SVG files
    babelTransformerPath: require.resolve('react-native-svg-transformer'),
  },
  resolver: {
    // Exclude 'svg' from asset extensions and add it to source extensions
    assetExts: assetExts.filter((ext) => ext !== 'svg'),
    sourceExts: [...sourceExts, 'svg'],
  },
};

// Merge the default configuration with the custom configuration
module.exports = mergeConfig(defaultConfig, config);

var fs = require('fs');
var common = require('./GIOCommon')


/* 
 * filePath: ReactNative的文件夹地址
 */
function injectReactNative(dirPath, reset=false){
	if(!dirPath.endsWith('/')){
		dirPath += '/';
	}
	var touchableJsFilePath = `${dirPath}Libraries/Components/Touchable/Touchable.js`
		console.log(`found and modify Touchable.js: ${touchableJsFilePath}`);
		injectOnPressScript(touchableJsFilePath);
	var createViewJsFiles = ['Libraries/Renderer/src/renderers/native/ReactNativeFiber.js',
        							 'Libraries/Renderer/src/renderers/native/ReactNativeFiber-dev.js',
        							 'Libraries/Renderer/src/renderers/native/ReactNativeFiber-prod.js',
        							 'Libraries/Renderer/src/renderers/native/ReactNativeFiber-profiling.js',
        							 'Libraries/Renderer/ReactNativeFiber-dev.js',
        							 'Libraries/Renderer/ReactNativeFiber-prod.js',
        							 'Libraries/Renderer/oss/ReactNativeRenderer-dev.js',
        							 'Libraries/Renderer/oss/ReactNativeRenderer-prod.js',
        							 'Libraries/Renderer/ReactNativeStack-dev.js',
        							 'Libraries/Renderer/ReactNativeStack-prod.js',
        							 'Libraries/Renderer/oss/ReactNativeRenderer-profiling.js',
        							 'Libraries/Renderer/ReactNativeRenderer-dev.js',
        							 'Libraries/Renderer/ReactNativeRenderer-prod.js',
        							 'Libraries/Renderer/implementations/ReactNativeRenderer-prod.js',
        							 'Libraries/Renderer/implementations/ReactNativeRenderer-dev.js'];
   createViewJsFiles.forEach(function(createViewFilePath){
   		var jsFile = `${dirPath}${createViewFilePath}`;
   		if(fs.existsSync(jsFile)){
   			    console.log(`found and modify render.js: ${jsFile}`);
   				injectCreateViewScript(jsFile);
   		}
   	});
}

/**
 * filePath: 对应的JS文件地址
 */
function injectCreateViewScript(filePath){
	common.modifyFile(filePath, createViewTransformer);
}

function injectOnPressScript(filePath){
	common.modifyFile(filePath, onPressTransformer);
}

function onPressTransformer(content){
	var index = content.indexOf('this.touchableHandlePress(');
	if(index == -1)
		throw "Can't not hook onPress function";
	var injectScript = "var ReactNative = require('react-native');\n" +
		"this.props.onPress&&ReactNative.NativeModules.RNSensorsAnalyticsModule.trackRNClick(ReactNative.findNodeHandle(this));"
	injectScript = common.anonymousJsFunctionCall(injectScript);
	var result = `${content.substring(0, index)}\n${injectScript}\n${content.substring(index)}`
	return result;
}

function createViewTransformer(content){
    var objRe = /ReactNativePrivateInterface\.UIManager\.createView\([\s\S]{1,60}\.uiViewClassName,[\s\S]*?\)[,;]/
    var match = objRe.exec(content);
    if(!match){
	objRe = /UIManager\.createView\([\s\S]{1,60}\.uiViewClassName,[\s\S]*?\)[,;]/
	match = objRe.exec(content);
	}
	if(!match)
		throw "can't inject createView, please connect with GrowingIO";
	var lastParentheses = content.lastIndexOf(')', match.index);
	var lastCommaIndex = content.lastIndexOf(',', lastParentheses);
	if(lastCommaIndex == -1)
		throw "can't inject createView,and lastCommaIndex is -1";
	var nextCommaIndex = content.indexOf(',', match.index);
	if(nextCommaIndex == -1)
		throw "can't inject createView, and nextCommaIndex is -1";
	var propsName = lastArgumentName(content, lastCommaIndex).trim();
	var tagName = lastArgumentName(content, nextCommaIndex).trim();
	//console.log(`propsName: ${propsName}, and tagName: ${tagName}`);
	var functionBody =
		`var clickable = false;
         if(${propsName}.onStartShouldSetResponder){
             clickable = true;
         }
         require('react-native').NativeModules.RNSensorsAnalyticsModule.prepareView(${tagName}, clickable);
                 `;
	var call = common.anonymousJsFunctionCall(functionBody);
	var lastReturn = content.lastIndexOf('return', match.index);
	var splitIndex = match.index;
	if(lastReturn > lastParentheses){
		splitIndex = lastReturn;
	}
	var result = `${content.substring(0, splitIndex)}\n${call}\n${content.substring(splitIndex)}`
	return result;
}

function lastArgumentName(content, index){
	--index;
	var lastComma = content.lastIndexOf(',', index);
	var lastParentheses = content.lastIndexOf('(', index);
	var start = Math.max(lastComma, lastParentheses);
	return content.substring(start + 1, index + 1);
}

module.exports = {
	injectCreateViewScript: injectCreateViewScript,
	createViewTransformer: createViewTransformer,
	injectOnPressScript: injectOnPressScript,
	injectReactNative: injectReactNative
}

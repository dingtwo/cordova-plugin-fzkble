#import <Cordova/CDV.h>

@interface HWPHello : CDVPlugin

- (void) greet:(CDVInvokedUrlCommand*)command;
// 连接车辆
- (void)connect:(CDVInvokedUrlCommand* )command;

// 开门
- (void)openDoor:(CDVInvokedUrlCommand *)command;

// 关门
- (void)closeDoor:(CDVInvokedUrlCommand *)command;

// 鸣笛
- (void)callCar:(CDVInvokedUrlCommand *)command;


- (void)resetBle:(CDVInvokedUrlCommand *)command;
@end

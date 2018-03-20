#import "HWPHello.h"
#import <FZKBLE/FZKBLE.h>
//#import "FZKBLE/SRBLECoder.h"
#import <CoreBluetooth/CoreBluetooth.h>
@interface HWPHello()<FZKBLEParseDelegate, CBCentralManagerDelegate>

@property (nonatomic,strong)FZKTBluetoothManager *bleManager;
// connect方法的id
@property (nonatomic, strong) CDVInvokedUrlCommand* connectCommand;

@property (nonatomic, strong) CBCentralManager* centralManager;

@property (nonatomic, strong) NSString *mac;
@property (nonatomic, strong) NSString *key;
@property (nonatomic, strong) NSString *bluetoothID;


@end
@implementation HWPHello


// 插件初始化
- (void)pluginInitialize
{
    [super pluginInitialize];
    NSLog(@"初始化插件");
    _bleManager = [FZKTBluetoothManager instanceShare];
    self.centralManager = [[CBCentralManager alloc] initWithDelegate:self queue:nil options:@{ CBCentralManagerOptionRestoreIdentifierKey:@"DJZRestoreIdentifier", CBCentralManagerOptionShowPowerAlertKey : @(YES)}];
    _bleManager.delegate = self;
}


- (void)greet:(CDVInvokedUrlCommand*)command
{

    // NSString* name = [[command arguments] objectAtIndex:0];
    // NSString* msg = [NSString stringWithFormat: @"Hello, %@", name];
	// NSLog(name);
    // CDVPluginResult* result = [CDVPluginResult
    //                            resultWithStatus:CDVCommandStatus_OK
    //                            messageAsString:msg];

    // [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];

	NSLog(@"开始连接");

    _bleManager = [FZKTBluetoothManager instanceShare];
    self.connectCommand = command;
    FZKTBluetoothInfoModel *ble = [[FZKTBluetoothInfoModel alloc] init];
    ble.mac = [[command arguments] objectAtIndex:0];
    ble.key = [[command arguments] objectAtIndex:1];
    ble.bluetoothID = [[command arguments] objectAtIndex:2];
    [_bleManager connection:ble];
    _bleManager.delegate = self;

}
- (void)connect:(CDVInvokedUrlCommand* )command {
    NSLog(@"开始连接");


    self.connectCommand = command;
    FZKTBluetoothInfoModel *ble = [[FZKTBluetoothInfoModel alloc] init];
    NSDictionary *args = [[command arguments] objectAtIndex:0];
    ble.mac = args[@"mac"];
    ble.key = args[@"key"];
    ble.bluetoothID = args[@"bluetoothID"];
    [_bleManager connection:ble];

}
- (void)resetBle:(CDVInvokedUrlCommand *)command {

    [[FZKTBluetoothManager instanceShare] disConnection];
    [FZKTBluetoothManager instanceShare].delegate = nil;
    self.centralManager.delegate = nil;
    self.centralManager = nil;
     CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"重置成功"];

     [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}
// 开门
- (void)openDoor:(CDVInvokedUrlCommand *)command {
    __block CDVPluginResult* result;
    if(![_bleManager connectionState]) {
        NSLog(@"与设备未连接");
    }else{
        [_bleManager sendCommand:SRBLEInstruction_Unlock callback:^(NSError *error, id responseObject) {
            if(error) {
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"蓝牙开门失败"];
                NSLog(@"%@", error);
            }else{
                NSLog(@"%@", responseObject);
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"蓝牙开门成功"];
            }
        }];
    }

     [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

// 关门
- (void)closeDoor:(CDVInvokedUrlCommand *)command {
    __block CDVPluginResult* result;
    if(![_bleManager connectionState]) {
        NSLog(@"与设备未连接");
    }else{
        [_bleManager sendCommand:SRBLEInstruction_Lock callback:^(NSError *error, id responseObject) {
            if(error) {
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"蓝牙开门失败"];
                NSLog(@"%@", error);
            }else{
                NSLog(@"%@", responseObject);
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"蓝牙开门成功"];
            }
        }];
    }

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

// 鸣笛
- (void)callCar:(CDVInvokedUrlCommand *)command {
    __block CDVPluginResult* result;
    if(![_bleManager connectionState]) {
        NSLog(@"与设备未连接");
    }else{
        [_bleManager sendCommand:SRBLEInstruction_Call callback:^(NSError *error, id responseObject) {
            if(error) {
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"蓝牙开门失败"];
                NSLog(@"%@", error);
            }else{
                NSLog(@"%@", responseObject);
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"蓝牙开门成功"];
            }
        }];
    }

    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)dealloc{
    NSLog(@"调用了插件的dealloc方法");
    [FZKTBluetoothManager instanceShare].delegate = nil;
    [[FZKTBluetoothManager instanceShare] disConnection];


}

- (void)initAndConnect {
    FZKTBluetoothInfoModel * ble = [[FZKTBluetoothInfoModel alloc] init];
    _bleManager = [FZKTBluetoothManager instanceShare];
    ble.mac = self.mac;
    ble.key = self.key;
    ble.bluetoothID = self.bluetoothID;
    [_bleManager connection:ble];
    _bleManager.delegate = self;
}

#pragma 蓝牙回调
- (void)centralManagerDidUpdateState:(CBCentralManager *)central {
    CDVPluginResult* result;
    switch (central.state) {
            case CBManagerStatePoweredOff:
            NSLog(@"请打开蓝牙");
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"请打开蓝牙"];

            break;
            case CBManagerStatePoweredOn:
            // 连接
            NSLog(@"可以连接");
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"开始连接"];
            break;
        default:
            break;
    }
    [self.commandDelegate sendPluginResult:result callbackId:self.connectCommand.callbackId];
}

- (void)centralManager:(CBCentralManager *)central willRestoreState:(NSDictionary<NSString *,id> *)dict {

}
- (void)bleConnectStatus:(FZKBLEConnectStatus)state {
    NSString *msg = @"";
//    CDVPluginResult* result;
//    switch (state) {
//            case FZKBLEConnectSuccess:
//            NSLog(@"已连接");
//            msg = @"已连接";
//            result = [CDVPluginResult
//                      resultWithStatus:CDVCommandStatus_OK
//                      messageAsString:msg];
//            break;
//            case FZKBLELoginSuccess:
//            NSLog(@"已连接并可控制");
//            result = [CDVPluginResult
//                      resultWithStatus:CDVCommandStatus_OK
//                      messageAsString:msg];
//            break;
//            case FZKBLEDisconnected:
//            NSLog(@"未连接");
//            break;
//            result = [CDVPluginResult
//                      resultWithStatus:CDVCommandStatus_ERROR
//                      messageAsString:msg];
//        default:
//            break;
//    }
//
//
//    [self.commandDelegate sendPluginResult:result callbackId:self.connectCommand.callbackId];
}

@end

package constant;

public class Menu {
  public static String MENU_SYSTEMS = 
          "=============================\n" 
           + "请选择应用：\n" 
           + "1. TrackGame\n"
           + "2. AtomStructure\n" 
           + "3. PersonalAppEcosystem\n" 
           + "4. 退出\n" 
           + "=============================\n";
  public static String ADDTRACK = "请输入轨道编号/半径添加新轨道：\n";
  public static String REMOVETRACK = "请输入轨道索引移除轨道：\n";
  public static String GROUP_INDEX = "请输入查看分组索引：\n";
  public static String ENTROPHY = "当前多轨道系统的熵值为：\n";
  public static String ERRORINPUT = "输入错误，请重新输入\n";
  public static String SUCCESS = "操作成功\n";
  public static String ERRORFILE = "文件错误，请重新选择\n";
  public static String MENU_TRACKGAME = 
          "=============================\n" 
          + "请选择操作：\n" 
          + "1. 图数据输入\n" 
          + "2. 比赛分组\n"
          + "3. 查看分组情况\n" 
          + "4. 调整比赛方案\n" 
          + "5. 查看某一分组\n" 
          + "0. 退出TrackGame\n" 
          + "============================\n";
  public static String INPUTCOMFIRM = "输入图输入会使之前分组不再保存，是否继续（y/n）：\n";
  public static String TRACKGAME_DIVIDER = "请选择分组策略：\n" + "1. 随机分组\n" + "2. 按照成绩由高到低排序\n";
  public static String TRACKGAME_EXCHANGE = "请输入调换的两个运动员号码（正整数 空格分开）：\n";
  public static String MENU_TRACKGAME_RACE = 
          "=============================\n" 
          + "请选择操作：\n" 
          + "1. 增加新的跑道\n"
          + "2. 向指定跑道上添加运动员\n" 
          + "3. 移除指定跑道\n" 
          + "4. 向指定跑道移除运动员\n" 
          + "5. 计算轨道熵值\n" 
          + "6. 可视化\n" 
          + "0. 返回\n"
          + "=============================\n";
  public static String TRACKGAME_ADDATHLETE = 
          "请输入运动员姓名，号码，国籍，年龄，最好成绩(<Name,Number,Nation,Age,Score>格式）和跑道索引，运动员和索引以 | 分开：\n";
  public static String TRACKGAME_REMOVEATHLETE = "请输入运动员编号和轨道索引，以 | 分割 ：\n";
  public static String MENU_ATOMSTRUCTURE = 
          "=============================\n" 
          + "请选择操作：\n" 
          + "1. 图数据输入\n"
          + "2. 增加新的轨道\n" 
          + "3. 向指定轨道添加指定数量电子,空格分开\n" 
          + "4. 移除指定轨道\n" 
          + "5. 向指定轨道移除指定数量电子,空格分开\n" 
          + "6. 电子跃迁\n"
          + "7. 恢复结构\n" 
          + "8. 计算轨道熵值\n" 
          + "9. 可视化\n" 
          + "0. 退出AtomStructure\n" 
          + "=============================\n";
  public static String ATOM_ADDELECTRON = "请输入指定轨道索引，从0开始和电子数量：\n";
  public static String ATOM_REMOVEELECTRON = "请输入指定轨道索引，从0开始和电子数量：\n";
  public static String ATOM_TRANSIT = "请输入指定原轨道和新轨道索引，从0开始：\n";
  public static String ATOM_BACKUP = "请输入备忘录编号或输入n返回\n";
  public static String MENU_APPECO = 
          "=============================\n" 
           + "请选择操作：\n" 
           + "1. 图数据输入\n" 
           + "2. 恢复结构（格式化）\n"
           + "3. 查看所有划分\n" 
           + "4. 查看某一划分\n" 
           + "5. 获取两个时间段轨道差异\n" 
           + "6. 计算APP之间逻辑距离\n" 
           + "0. 退出PersonalAppEcosystem\n"
           + "==============================\n";
  public static String APPECO_PERIOD = 
          "=============================\n" 
          + "请选择操作：\n" 
          + "1. 向指定轨道上添加App\n"
          + "2. 移除指定App\n" 
          + "3. 计算轨道熵值\n" 
          + "4. 可视化\n" 
          + "5. 生成描述字符串\n" 
          + "0. 返回\n" 
          + "=============================\n";
  public static String APPECO_ADDAPP = 
          "请输入App名称，公司，版本，描述，领域格式为<名称,公司,版本(._-d分割),\"描述\",\"领域\"> 和指定轨道索引，用 | 分割：\n";
  public static String APPECO_REMOVEAPP = "请输入App名称：\n";
  public static String APPEOC_TIMESPAN = 
          "请输入两个时间间隔，以空格划分，时间间隔格式为yyyy-mm-dd,hh:mm:ss=yyyy-mm-dd,hh:mm:ss\n";
  public static String APPECO_DISTANCE = "请输入APP的名称，空格分开：\n";
}

package Game;

/**
 * Created by thinking on 2016/11/14.
 */
public interface GameConstants {

//    String cm_ret_bool ="b";//���ز����ͽ��
//    String cm_updateServer="c";

    //�ָ���
    String SEP="#";



    //�����������������
    String ser_send_playerCount ="�����������������";

    //������������Ϸ״̬
    String ser_send_gameState ="������������Ϸ״̬";
    /*
    ��ȫ���˶�׼���ˣ���Ϸ��ʼ--�������ַ�---������ʱ����
     */
    //����������4�������ַ�  ***#1 2  3 4
    String ser_send_4num ="ser4num";
    //�������������table����
    String ser_table_remv_player="ȥ�����";//**#username
    String ser_table_incr_player="�������";//**#username score state
    String ser_table_inc_player_score ="�޸���ҷ���"; //**#username  score
    String ser_table_set_player_state="�޸������Ϸ״̬"; //**#username  state
    //������������������
    String ser_send_chat="��������";//**#username instence(end)

    //�����������ϣ����� ����ʱ�������ı�
    String ser_send_time="����ʱ����";//**#60

    /*
    ������ʱ�¼�
    1.����ǰ�����---����ʾһ�����ܵĽⷨ--����һ�ֵĿ�ʼ
    2.����ʱ����----����ʾһ�����ܵĽⷨ---����һ�ֵĿ�ʼ
    ������һ�������źű�ʾ
     */
    String ser_send_game_end ="��ǰ��Ϸ����";
//    //no use ������cm_client_state��ɹ���




    //    String cm_client_join="g";
//    String cm_client_create="f";

    /*
    ״̬����
     */
    //��ҵ���Ϸ״̬

    String player_not_inGame ="δ������Ϸ";//�����������Ϸ����
    String player_not_ready="δ׼��";
    String player_ready="��׼��";
    String player_gaming="��Ϸ��";

    //��ǰ��Ϸ״̬

    String game_not_created="��Ϸδ����";
    String game_created_and_can_join ="��Ϸ�Ѵ������ɼ���";
    String game_started="��Ϸ�ѿ�ʼ�����ɼ���";

    //�߼��ĳ���
    String TRUE="true",FALSE="false";


    /*************************
    * �ͻ��˷��͸���������ָ��
    * */
    //�ͻ��������Ƿ��û��ظ�
    String client_req_checkNameRepeat ="����û���";

    //�ͻ������������Ϸ����
    String client_req_renew_hall="�ͻ������������Ϸ����";

    //�ͻ��˵İ�ť�¼�
    /*
    �ͻ����ʹ�������Ϸ������--��������ͬ�Ⲣ������Ϸ��Ϣ---������������ok-->�ͻ��˸���table
     */
    String client_btn_create ="��Ҵ�������Ϸ";
    /*
    �ͻ��˷��ͼ�����Ϸ����--->�������������table
     */
    String client_btn_join ="��Ҽ�������Ϸ";

    //c-->s �������������table;
    String client_btn_ready ="���׼����";
    //����table���û����˵���������
    String client_btn_exit ="����˳���";
    //�����������������ݣ�����������пͻ�
    String client_btn_send_chat ="��ҷ�������";

    //�ɹ�����𰸣�������������ӷ���
    String client_req_inc_score="���ӷ���";


    //�������
    int div_error=999;
    String add="+";
    String sub="-";
    String mul="*";
    String div="/";
    String left="(";
    String right=")";
    int total_time=90;


}

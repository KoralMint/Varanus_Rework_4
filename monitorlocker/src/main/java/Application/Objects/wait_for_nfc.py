import multiprocessing
import nfcreaders

# グローバル変数としてプロセスを定義
p = None

def worker():
    try:
        x=nfcreaders.NFCTagReader()
        a=x.wait_for_contact()
        b = nfcreaders.T3TData(a).id # .T3TData(a).id / .T3TData(a).json()でも動く
        print(b) #str型
    except:
        print("-2") # tag3が存在しないか、リーダーが存在しない
    terminate_process()  # プロセスを終了
    
def terminate_process():
    global p
    # プロセスがまだ実行中の場合
    if p and p.is_alive():
        # print("process finished.") 
        p.terminate()  # プロセスを強制終了
        p.join()  # プロセスが完全に終了するまで待機
    else:
        pass

def main():
    global p
    # ワーカープロセスを生成
    p = multiprocessing.Process(target=worker)

    # プロセスを開始
    p.start()

    # タイムアウトの秒数
    timeout_seconds = 10
    
    # タイムアウト秒数だけ待機
    
    for i in range(timeout_seconds*2):
        
        if p.is_alive():
            p.join(0.5)
        else:
            break
    
    if p.is_alive():
        print("-1") #timeout
        terminate_process()  # プロセスを終了

if __name__ == "__main__":
    main()

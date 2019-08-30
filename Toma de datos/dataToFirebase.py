import serial, time, firebase_admin, datetime, os
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate("eenergiasctpi-firebase-adminsdk-84r6q-8cb21c1dc6.json")
app1 = firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://eenergiasctpi.firebaseio.com/'
})

ref = db.reference('tarjeta1/tiempoReal',app1)
refData = db.reference('tarjeta1/datos',app1)
snapshot= ref.get()

i=0
try:
    print(str(i))
    print(len(snapshot))
    i=len(snapshot)
    if i==60:
        i=0
        pass
except Exception as e:
    print("Error de datos")
    pass

ser = serial.Serial('/dev/ttyACM0', 115200)
def loop():
    data = ""
    try:
        if os.path.exists('/dev/ttyACM0'):
            print(">>> Usando ttyACM0")
            data = ser.readline()
        elif os.path.exists('/dev/ttyACM1'):
            print(">>> Usando ttyACM1")
            
            data = ser.readline()
        elif os.path.exists('/dev/ttyACM2'):
            print(">>> Usando ttyACM2")
            
            data = ser.readline()
        elif os.path.exists('/dev/ttyACM3'):
            print(">>> Usando ttyACM3")
            
            data = ser.readline()
        else:
            print("Desconectado o cambio de puerto.")
    except Exception as e:
        print("Error: ", e)
    return data

while True:

    inf = loop()
    print(inf)

    try:
        tiempo=0;
        fechaNow = datetime.datetime.now()
        dia = str(fechaNow.day)
        mes = str(fechaNow.month)
        anno = str(fechaNow.year)
        hora = str(fechaNow.strftime("%I"))+":"+str((fechaNow.strftime("%M")))+" "+str((fechaNow.strftime("%p")))
        fechaActual = dia+"-"+mes+"-"+anno
        fechaActual1 =dia+"-"+mes+"-"+anno+" "+str(fechaNow.strftime("%H"))+":"+str((fechaNow.strftime("%M")))+":"+str((fechaNow.strftime("%S")))
    except Exception as e:
        print("Error de datos")
        pass
        
    try:
        dato1= str(inf).split("= ")[1].split("A")[0]
        dato2= str(inf).split("= ")[2].split("W")[0]
        dato3= str(inf).split("= ")[3].split("A")[0]
        dato4= str(inf).split("= ")[4].split("W")[0]
        dato5= str(inf).split("= ")[5].split("A")[0]
        dato6= str(inf).split("= ")[6].split("W")[0]
        ref.child(str(i)).set({
            'fechaActual':fechaActual,
            'hora':hora,
            'fechaActual1':fechaActual1,
            'corriente1':dato1,
            'corriente2':dato3,
            "corriente3" :dato5,
            "potencia1" : dato2,
            "potencia2" : dato4,
            "potencia3" : dato6,

        })

        
        if i==59:
            nowRef = refData.child("y"+anno).child("m"+mes).child("d"+dia)
            i=0
            j=0
            k=0
            try:
                j= len(nowRef.get())
                k= len(nowRef.get())
                pass
            except Exception as a:
                pass
            nowRef.child(str(j)).set({
                'hora':hora,
                'corriente1':dato1,
                'corriente2':dato3,
                "corriente3" :dato5,
                "potencia1" : dato2,
                "potencia2" : dato4,
                "potencia3" : dato6,

            })
        else:
            i=i+1
    except Exception as e:
        print("Error de datos")
        pass
    
    
    time.sleep(1)

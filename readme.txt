명령어
	관리자
		givetolocker
			플레이어 닉네임 미입력시 모든 유저의 인벤토리에 손에 있는 아이템을 넣고
			만약에 인벤토리가 꽉 차있는 상태라면 보관함으로 들어가게 된다.
		lockersave
			모든 플레이어의 보관함 상태를 저장한다. 서버 정상 종료시에도 자동으로 저장한다.
		lokcerautosave
			명령어만 입력시 자동저장이 켜져 있는지 확인한다.
			on또는 true 입력시 자동저장을 킨다.
			off또는 false 입력시 자동저장을 끈다.
		lockerautomessage
			명령어만 입력시 자동수령알림이 켜져 있는지 확인한다.
			on또는 true 입력시 자동수령알림을 킨다.
			off또는 false 입력시 자동수령알림을 끈다.
		lockerautotime
			명령어만 입력시 자동시스템의 시간을 확인한다.
			숫자 입력시 자동시스템의 간격을 입력한 숫자(분)으로 변경한다.

	유저
		보관함
		locker
			자신의 보관함을 연다.
			아이템을 옮길 수 없으며 보관함의 아이템 클릭 시 인벤토리에 공간이 있다면
			해당 아이템을 인벤토리에 넣는다.
			보관함을 보던중 보관함에 새로운 아이템이 들어온다면 증발 방지로 인하여
			보관함을 강제로 닫게 한다.
			보관함에 보여지는 칸을 초과하더라도 기존 아이템 클릭 시 다음 아이템이 계속해서 보여진다.
			(보관함에 있는 아이템을 습득한 후 보관함을 닫았다가 열어야 함)

자동시스템
	1. 전체 유저 보관함 저장
	2. 보관함에 아이템이 있는 유저에게 메세지

권한
	LordOfTheLocker.op.*
		LordOfTheLocker.op.givetolocker
		LordOfTheLocker.op.lockersave
		LordOfTheLocker.op.lockerautosave
		LordOfTheLocker.op.lockerautomessage
		LordOfTheLocker.op.lockerautotime
		
	LordOfTheLocker.user.*
		LordOfTheLocker.user.locker
		LordOfTheLocker.user.보관함
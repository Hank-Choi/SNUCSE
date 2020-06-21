	.option nopic
	.attribute arch, "rv64i2p0_m2p0_a2p0_f2p0_d2p0_c2p0"
	.attribute unaligned_access, 0
	.attribute stack_align, 16
	.text


	.align	1
	.globl	solve_maze
	.type	solve_maze, @function
solve_maze:
	#------Your code starts here------
	#maze: a0, width: a1, height: a2
	addi sp,sp,-72
	sd	ra,	64(sp)
	sd	s8,	56(sp)
	sd	s1,	48(sp)
	sd	s2,	40(sp)
	sd	s3,	32(sp)
	sd	s4,	24(sp)
	sd	s5,	16(sp)
	sd	s6,	8(sp)
	sd	s7,	0(sp)
	addi s8,zero,20
	addi s1,zero,0
	addi s2,zero,1
	addi s3,zero,2
	addi s4,zero,3
	addi s5,a0,0
	addi s6,a1,0
	addi s7,a2,0
	addi a0,zero,0
	addi a1,zero,0
	addi a2,zero,0
	addi a3,zero,2
	jal	ra,	your_funct
	ld	s7,	0(sp)
	ld	s6,	8(sp)
	ld	s5,	16(sp)
	ld	s4,	24(sp)
	ld	s3,	32(sp)
	ld	s2,	40(sp)
	ld	s1,	48(sp)
	ld	s8,	56(sp)
	ld	ra,	64(sp)
	addi sp,sp,72

	#Load return value to reg a0
	#------Your code ends here------

	#Ret
	jr ra
	.size	solve_maze, .-solve_maze



	#------You can declare additional functions here
	.align	1
	.globl	your_funct
	.type	your_funct, @function
your_funct:
	bge a2,s8,Noway
	blt a0,zero,Noway
	blt a1,zero,Noway
	bge a0,s6,Noway
	bge a1,s7,Noway
	mul t0,a1,s6
	add t0,t0,a0
	slli t1,t0,3
	add t1,t1,s5
	ld t2,0(t1)
	bne t2,zero,Noway
	addi t3,s6,-1
	addi t4,s7,-1
	bne t3,a0,continue
	bne t4,a1,continue
success:
	addi a0,a2,0
	jr ra
Noway:
	addi a0,zero,-1
	jr ra
continue:
	addi t5,zero,-1
T_up:
	beq a3,s4,T_left
	addi sp,sp,-40
	sd a0,32(sp)
	sd a1,24(sp)
	sd a2,16(sp)
	sd a3,8(sp)
	sd ra,0(sp)
	addi a1,a1,-1
	addi a2,a2,1
	addi a3,s1,0
	jal ra,your_funct
	add t5,a0,zero
	ld ra,0(sp)
	ld a3,8(sp)
	ld a2,16(sp)
	ld a1,24(sp)
	ld a0,32(sp)
	addi sp,sp,40
T_left:
	beq	a3,s3,T_right
	addi sp,sp,-40
	sd	a0,32(sp)
	sd	a1,24(sp)
	sd	a2,16(sp)
	sd	a3,8(sp)
	sd	ra,0(sp)
	addi a0,a0,-1
	addi a2,a2,1
	addi a3,s2,0
	jal	ra,your_funct
	add	t1,a0,zero
	ld	ra,0(sp)
	ld	a3,8(sp)
	ld	a2,16(sp)
	ld	a1,24(sp)
	ld	a0,32(sp)
	addi sp,sp,40
	blt	t1,zero,T_right
	blt	t5,zero,renew1
	blt	t1,t5,renew1
	j	T_right
renew1:
	addi t5,t1,0

T_right:
	beq	a3,s2,T_down
	
	addi sp,sp,-40
	sd	a0,32(sp)
	sd	a1,24(sp)
	sd	a2,16(sp)
	sd	a3,8(sp)
	sd	ra,0(sp)
	addi a0,a0,1
	addi a2,a2,1
	addi a3,s3,0
	jal ra,your_funct
	add t1,a0,zero
	ld	ra,0(sp)
	ld	a3,8(sp)
	ld	a2,16(sp)
	ld	a1,24(sp)
	ld	a0,32(sp)
	addi sp,sp,40
	blt	t1,zero,T_down
	blt	t5,zero,renew2
	blt	t1,t5,renew2
	j	T_down
renew2:
	addi t5,t1,0
T_down:
	beq a3,s1,Ret_min
	addi sp,sp,-40
	sd	a0,32(sp)
	sd	a1,24(sp)
	sd	a2,16(sp)
	sd	a3,8(sp)
	sd	ra,0(sp)
	addi a1,a1,1
	addi a2,a2,1
	addi a3,s4,0
	jal	ra,your_funct
	add	t1,a0,zero
	ld	ra,0(sp)
	ld	a3,8(sp)
	ld	a2,16(sp)
	ld	a1,24(sp)
	ld	a0,32(sp)
	addi sp,sp,40
	blt	t1,zero,Ret_min
	blt	t5,zero,renew3
	blt	t1,t5,renew3
	j	Ret_min
renew3:
	addi t5,t1,0
Ret_min:
	#Ret
	addi a0,t5,0
	jr	ra
	.size	your_funct, .-your_funct
	#------Your code ends here






	.ident	"GCC: (GNU) 9.2.0"

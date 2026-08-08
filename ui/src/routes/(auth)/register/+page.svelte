<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import AuthCard from '../AuthCard.svelte';
</script>

<AuthCard backPath="/login" backTitle="Already have an account?" title="Register">
	<div class="w-full">
		<Input
			name="username"
			autocomplete="username"
			hint="Username is required"
			inputClass="w-full"
			placeholder="Username*"
			required
		/>
	</div>

	<div class="w-full">
		<Input
			name="email"
			autocomplete="email"
			hint="E-Mail is required"
			inputClass="w-full"
			placeholder="E-Mail*"
			required
		/>
	</div>

	<div class="w-full">
		<Input
			name="password"
			autocomplete="new-password"
			hint="Password is required"
			inputClass="w-full"
			placeholder="Password*"
			required
			type="password"
		/>
	</div>

	<div class="w-full">
		<Input
			name="confirmedPassword"
			autocomplete="new-password"
			hint="Password is required"
			inputClass="w-full"
			placeholder="Repeat Password*"
			required
			type="password"
		/>
	</div>

	<div class="mt-4 w-full">
		<Button class="w-full btn-primary" label="Register" type="submit" />
	</div>
</AuthCard>

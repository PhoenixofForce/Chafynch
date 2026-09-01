// @ts-check
import { defineConfig } from "astro/config";
import starlight from "@astrojs/starlight";

// https://astro.build/config
export default defineConfig({
	integrations: [
		starlight({
			title: "Chafynch",
			customCss: ["./src/styles/custom.gen.css"],
			social: [
				{
					icon: "github",
					label: "GitHub",
					href: "https://github.com/PhoenixofForce/Chafynch/",
				},
			],
			sidebar: [
				{
					label: "Guides",
					items: [
						// Each item here is one entry in the navigation menu.
						{ label: "Example Guide", slug: "guides/example" },
					],
				},
				{
					label: "Reference",
					items: [{ autogenerate: { directory: "reference" } }],
				},
				{
					label: "Stars",
					items: [{ autogenerate: { directory: "stars" } }],
				},
			],
		}),
	],
});
